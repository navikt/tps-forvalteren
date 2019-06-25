package no.nav.tps.forvalteren.service.command.avspiller;

import static java.lang.Long.valueOf;
import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspiller;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspillerProgress;
import no.nav.tps.forvalteren.domain.rs.Meldingsformat;
import no.nav.tps.forvalteren.domain.rs.RsAvspillerRequest;
import no.nav.tps.forvalteren.domain.rs.RsMeldingerResponse;
import no.nav.tps.forvalteren.domain.rs.RsTyperOgKilderResponse;
import no.nav.tps.forvalteren.domain.rs.TypeOgAntall;
import no.nav.tps.forvalteren.domain.rs.TypeOppslag;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsDistribusjonsmeldingService;
import no.nav.tps.xjc.ctg.domain.s302.EnkeltMeldingType;
import no.nav.tps.xjc.ctg.domain.s302.HendelsedataFraTpsS302;
import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;
import no.nav.tps.xjc.ctg.domain.s302.TpsServiceRutineType;

@Slf4j
@Service
@EnableAsync
public class AvspillerService {

    private static final String NO_DATA_KEY = "avspiller.request.nodata";
    private static final String PING_TEST = "avspiller.queue.ping.test";
    private static final String MORE_MSG_AVAIL = "S302006I";

    @Autowired
    private TpsDistribusjonsmeldingService tpsDistribusjonsmeldingService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private AvspillerDaoService avspillerDaoService;

    @Autowired
    private MessageProvider messageProvider;

    public RsTyperOgKilderResponse getTyperOgKilder(RsAvspillerRequest request) {

        RsTyperOgKilderResponse typerOgKilderResponse = new RsTyperOgKilderResponse();

        request.setBufferSize(60L);
        Long pageNumber = 0L;
        TpsPersonData fraTps;
        do {
            request.setPageNumber(pageNumber++);
            fraTps = getHendelsedataFraTps(request, TypeOppslag.O);

            if (nonNull(fraTps.getTpsSvar().getHendelseDataS302())) {
                HendelsedataFraTpsS302 response = fraTps.getTpsSvar().getHendelseDataS302().getRespons();

                if (nonNull(response.getOversiktKilde())) {
                    response.getOversiktHendelse().getEnoversiktHendelse().forEach(hendelseType ->
                            typerOgKilderResponse.getTyper().add(TypeOgAntall.builder()
                                    .antall(valueOf(hendelseType.getOversiktMeldingTypeAntall()))
                                    .type(hendelseType.getOversiktMeldingType())
                                    .build())
                    );
                }
                if (nonNull(response.getOversiktKilde())) {
                    response.getOversiktKilde().getEnoversiktKilde().forEach(kildeType ->
                            typerOgKilderResponse.getKilder().add(TypeOgAntall.builder()
                                    .antall(valueOf(kildeType.getOversiktKildeSystemAntall()))
                                    .type(kildeType.getOversiktKildeSystem())
                                    .build())
                    );
                }
            } else {
                throw new NotFoundException(messageProvider.get(NO_DATA_KEY, request.getMiljoeFra(), request.getDatoFra(), request.getDatoTil()));
            }
        }
        while (MORE_MSG_AVAIL.equals(fraTps.getTpsSvar().getSvarStatus().getReturMelding()));

        return typerOgKilderResponse;
    }

    public RsMeldingerResponse getMeldinger(RsAvspillerRequest request) {

        TpsPersonData fraTps = getHendelsedataFraTps(request, TypeOppslag.L);

        if (nonNull(fraTps.getTpsSvar().getHendelseDataS302()) &&
                valueOf(fraTps.getTpsSvar().getHendelseDataS302().getRespons().getMeldingerTotalt()) > 0) {
            return mapperFacade.map(fraTps.getTpsSvar().getHendelseDataS302().getRespons(), RsMeldingerResponse.class);

        } else {

            throw new NotFoundException(messageProvider.get(NO_DATA_KEY, request.getMiljoeFra(), request.getDatoFra(), request.getDatoTil()));
        }
    }

    @Async
    public void sendTilTps(RsAvspillerRequest request, TpsAvspiller avspillerStatus) {

        TpsPersonData personListe = null;

        try {
            Long pageNumber = 0L;
            request.setPageNumber(pageNumber++);
            request.setBufferSize(150L);
            TpsPersonData tpsPersonData = mapperFacade.map(request, TpsPersonData.class);
            tpsPersonData.getTpsServiceRutine().setTypeOppslag(TypeOppslag.L.name());

            do {
                tpsPersonData.getTpsServiceRutine().setSideNummer(Long.toString(pageNumber++));
                personListe = tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(tpsPersonData, request.getMiljoeFra(), request.getTimeout());

                avspillerStatus = logProgress(avspillerStatus.getBestillingId(), personListe, false, null);

                for (int i = 0; i < personListe.getTpsSvar().getHendelseDataS302().getRespons().getMelding().getEnmelding().size(); i++) {

                    EnkeltMeldingType enkeltMeldingType = personListe.getTpsSvar().getHendelseDataS302().getRespons().getMelding().getEnmelding().get(i);
                    TpsPersonData detaljertMelding = getDetaljertMelding(request, enkeltMeldingType.getMNr());

                    String status = tpsDistribusjonsmeldingService.sendDetailedMessageToTps(
                            detaljertMelding.getTpsSvar().getHendelseDataS302().getRespons().getMeldingDetalj(),
                            request.getMiljoeTil(),
                            request.getQueue(),
                            request.getFormat() == Meldingsformat.AJOURHOLDSMELDING);

                    avspillerStatus = logProgress(avspillerStatus.getBestillingId(),
                            (valueOf(personListe.getTpsSvar().getHendelseDataS302().getRespons().getSideNummer()) - 1) *
                                    valueOf(personListe.getTpsSvar().getHendelseDataS302().getRespons().getAntallRaderprSide()) + i + 1,
                            enkeltMeldingType,
                            decodeStatus(status));

                    log.debug(decodeStatus(status));
                    if (avspillerStatus.isAvbrutt()) {
                        break;
                    }
                }
            }
            while (MORE_MSG_AVAIL.equals(personListe.getTpsSvar().getSvarStatus().getReturMelding()) && !avspillerStatus.isAvbrutt());

            logProgress(avspillerStatus.getBestillingId(), personListe, true, null);

        } catch (RuntimeException e) {
            logProgress(avspillerStatus.getBestillingId(), personListe, true, e.getMessage());
        }
    }

    public boolean isValid(String environment, String queueName) {
        String result = tpsDistribusjonsmeldingService.sendDetailedMessageToTps(messageProvider.get(PING_TEST), environment, queueName, false);
        return !result.contains(queueName);
    }

    private TpsAvspiller logProgress(Long bestillingId, TpsPersonData personData, boolean isFerdig, String error) {

        TpsAvspiller status = avspillerDaoService.getStatus(bestillingId);
        status.setTidspunkt(now());
        status.setFerdig(isFerdig);
        status.setAntall(valueOf(personData.getTpsSvar().getHendelseDataS302().getRespons().getMeldingerTotalt()));
        status.setFeil(error);

        return avspillerDaoService.save(status);
    }

    private TpsAvspiller logProgress(Long bestillingId, Long indeksNr, EnkeltMeldingType enkeltMelding, String sendStatus) {

        avspillerDaoService.save(
                TpsAvspillerProgress.builder()
                        .indeksNr(indeksNr)
                        .meldingNr(valueOf(enkeltMelding.getMNr()))
                        .bestillingId(bestillingId)
                        .tidspunkt(now())
                        .sendStatus(sendStatus)
                        .build());

        return avspillerDaoService.getStatus(bestillingId);
    }

    private static String decodeStatus(String status) {

        if (isBlank(status)) {
            return "Sending OK, ukjent mottakstatus";
        } else if (status.contains("00;") || (status.contains("04;"))) {
            return "OK";
        } else if (status.contains("08;") || (status.contains("12;"))) {
            return "FEIL: " + status.substring(3).trim();
        } else {
            return "FEIL: " + status.trim();
        }
    }

    private TpsPersonData getDetaljertMelding(RsAvspillerRequest request, String meldingNummer) {

        TpsPersonData tpsPersonData = mapperFacade.map(request, TpsPersonData.class);
        tpsPersonData.getTpsServiceRutine().setTypeOppslag(TypeOppslag.H.name());
        tpsPersonData.getTpsServiceRutine().setMeldingNummer(meldingNummer);

        return tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(tpsPersonData, request.getMiljoeFra(), request.getTimeout());
    }

    private TpsPersonData getHendelsedataFraTps(RsAvspillerRequest request, TypeOppslag typeOppslag) {

        TpsPersonData tpsPersonData = mapperFacade.map(request, TpsPersonData.class);
        tpsPersonData.getTpsServiceRutine().setTypeOppslag(typeOppslag.name());

        return tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(tpsPersonData, request.getMiljoeFra(), request.getTimeout());
    }

    public String showRequest(String miljoe, Meldingsformat format, String meldingnr) {

        TpsPersonData tpsPersonData = new TpsPersonData();
        TpsServiceRutineType tpsServiceRutineType = new TpsServiceRutineType();
        tpsServiceRutineType.setMeldingFormat(format.getMeldingFormat());
        tpsServiceRutineType.setTypeOppslag(TypeOppslag.H.name());
        tpsServiceRutineType.setMeldingNummer(meldingnr);
        tpsPersonData.setTpsServiceRutine(tpsServiceRutineType);

        try {
            TpsPersonData response = tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(tpsPersonData, miljoe, 1L);
            return response.getTpsSvar().getHendelseDataS302().getRespons().getMeldingDetalj();
        } catch (TpsfFunctionalException e) {
            return e.getMessage();
        }
    }
}
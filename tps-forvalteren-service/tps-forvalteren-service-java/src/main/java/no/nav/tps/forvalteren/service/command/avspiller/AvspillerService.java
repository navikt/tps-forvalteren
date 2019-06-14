package no.nav.tps.forvalteren.service.command.avspiller;

import static java.lang.Long.valueOf;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.avspiller.AvspillerConvertUtils.convertIdenter;
import static no.nav.tps.forvalteren.service.command.avspiller.AvspillerConvertUtils.convertKildeSystem;
import static no.nav.tps.forvalteren.service.command.avspiller.AvspillerConvertUtils.convertMeldingType;
import static no.nav.tps.forvalteren.service.command.avspiller.AvspillerConvertUtils.extractBuffernumber;
import static no.nav.tps.forvalteren.service.command.avspiller.AvspillerConvertUtils.extractBuffersize;
import static no.nav.tps.forvalteren.service.command.avspiller.AvspillerConvertUtils.extractDateFrom;
import static no.nav.tps.forvalteren.service.command.avspiller.AvspillerConvertUtils.extractDateTo;
import static no.nav.tps.forvalteren.service.command.avspiller.AvspillerConvertUtils.extractList;
import static no.nav.tps.forvalteren.service.command.avspiller.AvspillerConvertUtils.extractTimeFrom;
import static no.nav.tps.forvalteren.service.command.avspiller.AvspillerConvertUtils.extractTimeeTo;
import static no.nav.tps.forvalteren.service.command.avspiller.AvspillerConvertUtils.unpackPeriode;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspiller;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspillerProgress;
import no.nav.tps.forvalteren.domain.rs.Meldingsformat;
import no.nav.tps.forvalteren.domain.rs.RsAvspillerRequest;
import no.nav.tps.forvalteren.domain.rs.RsMeldingerResponse;
import no.nav.tps.forvalteren.domain.rs.RsTyperOgKilderResponse;
import no.nav.tps.forvalteren.domain.rs.TypeOgAntall;
import no.nav.tps.forvalteren.domain.rs.TypeOppslag;
import no.nav.tps.forvalteren.domain.rs.skd.RsAvspillerProgress;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsDistribusjonsmeldingService;
import no.nav.tps.xjc.ctg.domain.s302.EnkeltMeldingType;
import no.nav.tps.xjc.ctg.domain.s302.HendelsedataFraTpsS302;
import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;
import no.nav.tps.xjc.ctg.domain.s302.TpsServiceRutineType;

@Slf4j
@Service
public class AvspillerService {

    private static final String NO_DATA = "Ingen data for miljø %s i periode fra %s ble funnet";
    private static final String JSON_ERROR = "Konvertering til Json feilet";

    @Autowired
    private TpsDistribusjonsmeldingService tpsDistribusjonsmeldingService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AvspillerDaoService avspillerDaoService;

    public RsTyperOgKilderResponse getTyperOgKilder(Meldingsformat format, String periode, String miljoe) {

        TpsPersonData fraTps = getHendelsedataFraTps(miljoe, periode, format, null, null, null, null);

        if (nonNull(fraTps.getTpsSvar().getHendelseDataS302())) {
            HendelsedataFraTpsS302 response = fraTps.getTpsSvar().getHendelseDataS302().getRespons();

            RsTyperOgKilderResponse typerOgKilderResponse = new RsTyperOgKilderResponse();
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

            return typerOgKilderResponse;

        } else {

            throw new NotFoundException(String.format(NO_DATA, miljoe, unpackPeriode(periode)));
        }
    }

    public RsMeldingerResponse getMeldinger(String miljoe, String periode, Meldingsformat format, String meldingstyper, String kilder, String identer, String buffer) {

        TpsPersonData fraTps = getHendelsedataFraTps(miljoe, periode, format, meldingstyper, kilder, identer, buffer);

        if (nonNull(fraTps.getTpsSvar().getHendelseDataS302())) {
            return mapperFacade.map(fraTps.getTpsSvar().getHendelseDataS302().getRespons(), RsMeldingerResponse.class);

        } else {

            throw new NotFoundException(String.format(NO_DATA, miljoe, unpackPeriode(periode)));
        }
    }

    @Async
    public void sendTilTps(RsAvspillerRequest request) {

        TpsAvspiller avspillerStatus = null;
        TpsPersonData personListe;
        Long pageNumber = 0L;

        TpsPersonData tpsPersonData = mapperFacade.map(request, TpsPersonData.class);
        tpsPersonData.getTpsServiceRutine().setTypeOppslag(TypeOppslag.L.name());

        do {
            tpsPersonData.getTpsServiceRutine().setSideNummer(Long.toString(++pageNumber));
            personListe = tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(tpsPersonData, request.getMiljoeFra());

            avspillerStatus = logProgress(avspillerStatus, request, personListe);

            for (int i = 0; i < personListe.getTpsSvar().getHendelseDataS302().getRespons().getMelding().getEnmelding().size(); i++) {

                EnkeltMeldingType enkeltMeldingType = personListe.getTpsSvar().getHendelseDataS302().getRespons().getMelding().getEnmelding().get(i);
                TpsPersonData detaljertMelding = getDetaljertMelding(request, enkeltMeldingType.getMNr());

                String status = tpsDistribusjonsmeldingService.sendDetailedMessageToTps(
                        detaljertMelding.getTpsSvar().getHendelseDataS302().getRespons().getMeldingDetalj(),
                        request.getMiljoeTil(),
                        request.getQueue(),
                        request.getFormat() == Meldingsformat.Ajourholdsmelding);

                logProgress(avspillerStatus,
                        (valueOf(personListe.getTpsSvar().getHendelseDataS302().getRespons().getSideNummer()) - 1) *
                                valueOf(personListe.getTpsSvar().getHendelseDataS302().getRespons().getAntallRaderprSide()) + i + 1,
                        enkeltMeldingType,
                        status);

                log.info(status);
            }
        }
        while ("S302006I".equals(personListe.getTpsSvar().getSvarStatus().getReturMelding()));

        avspillerStatus.setFerdig(true);
        logProgress(avspillerStatus, request, personListe);
    }

    public List<RsAvspillerProgress> getStatuser(Long bestillingId) {
        return null;
    }

    private TpsAvspiller logProgress(TpsAvspiller status, RsAvspillerRequest request, TpsPersonData personData) {

        if (nonNull(status)) {
            status.setTidspunkt(now());
        } else {
            status = TpsAvspiller.builder()
                    .ferdig(false)
                    .format(request.getFormat().getMeldingFormat())
                    .kildeKoe(request.getMiljoeFra())
                    .periodeFra(request.getDatoFra())
                    .periodeTil(request.getDatoTil())
                    .utloepKoe(request.getQueue())
                    .antall(valueOf(personData.getTpsSvar().getHendelseDataS302().getRespons().getMeldingerTotalt()))
                    .request(toJson(request))
                    .tidspunkt(now())
                    .build();
        }
        return avspillerDaoService.save(status);
    }

    private TpsAvspillerProgress logProgress(TpsAvspiller avspillerStatus, Long indeksNr, EnkeltMeldingType enkeltMelding, String sendStatus) {
        return avspillerDaoService.save(
                TpsAvspillerProgress.builder()
                        .indeksNr(indeksNr)
                        .meldingNr(valueOf(enkeltMelding.getMNr()))
                        .bestillingId(avspillerStatus.getBestillingId())
                        .tidspunkt(now())
                        .sendStatus(isBlank(sendStatus) ? "Sending OK, ukjent mottakstatus" : sendStatus)
                        .build());
    }

    private TpsPersonData getDetaljertMelding(RsAvspillerRequest request, String meldingNummer) {

        TpsPersonData tpsPersonData = mapperFacade.map(request, TpsPersonData.class);
        tpsPersonData.getTpsServiceRutine().setTypeOppslag(TypeOppslag.H.name());
        tpsPersonData.getTpsServiceRutine().setMeldingNummer(meldingNummer);

        return tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(tpsPersonData, request.getMiljoeFra());
    }

    private TpsPersonData getHendelsedataFraTps(
            String miljoe, String periode, Meldingsformat format, String meldingstyper, String kilder, String identer, String buffer) {

        TpsServiceRutineType tpsServiceRutineType = new TpsServiceRutineType();
        tpsServiceRutineType.setTypeOppslag(isNull(buffer) ? TypeOppslag.O.name() : TypeOppslag.L.name());
        tpsServiceRutineType.setMeldingFormat(format.getMeldingFormat());
        tpsServiceRutineType.setFraDato(extractDateFrom(periode));
        tpsServiceRutineType.setFraTid(extractTimeFrom(periode));
        tpsServiceRutineType.setTilDato(extractDateTo(periode));
        tpsServiceRutineType.setTilTid(extractTimeeTo(periode));
        tpsServiceRutineType.setSideNummer(extractBuffernumber(buffer));
        tpsServiceRutineType.setAntallRaderprSide(extractBuffersize(buffer));
        tpsServiceRutineType.setMeldingType(convertMeldingType(extractList(meldingstyper)));
        tpsServiceRutineType.setKildeSystem(convertKildeSystem(extractList(kilder)));
        tpsServiceRutineType.setFnr(convertIdenter(extractList(identer)));

        TpsPersonData tpsPersonData = new TpsPersonData();
        tpsPersonData.setTpsServiceRutine(tpsServiceRutineType);
        return tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(tpsPersonData, miljoe);
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writer().writeValueAsString(object);
        } catch (JsonProcessingException | RuntimeException e) {
            log.debug(JSON_ERROR, e);
        }
        return JSON_ERROR;
    }
}
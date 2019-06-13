package no.nav.tps.forvalteren.service.command.avspiller;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.util.Lists.newArrayList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.rs.Meldingsformat;
import no.nav.tps.forvalteren.domain.rs.RsAvspillerRequest;
import no.nav.tps.forvalteren.domain.rs.RsMelding;
import no.nav.tps.forvalteren.domain.rs.RsMeldingerResponse;
import no.nav.tps.forvalteren.domain.rs.RsTyperOgKilderResponse;
import no.nav.tps.forvalteren.domain.rs.TypeOgAntall;
import no.nav.tps.forvalteren.domain.rs.TypeOppslag;
import no.nav.tps.forvalteren.domain.rs.skd.RsAvspillerProgress;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsDistribusjonsmeldingService;
import no.nav.tps.xjc.ctg.domain.s302.HendelsedataFraTpsS302;
import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;
import no.nav.tps.xjc.ctg.domain.s302.TpsServiceRutineType;

@Slf4j
@Service
public class AvspillerService {

    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String NO_DATA = "Ingen data for miljø %s i periode fra %s ble funnet";

    @Autowired
    private TpsDistribusjonsmeldingService tpsDistribusjonsmeldingService;

    @Autowired
    private MapperFacade mapperFacade;

    public RsTyperOgKilderResponse getTyperOgKilder(Meldingsformat format, String periode, String miljoe) {

        TpsPersonData fraTps = getHendelsedataFraTps(miljoe, periode, format, null, null, null, null);

        if (nonNull(fraTps.getTpsSvar().getHendelseDataS302())) {
            HendelsedataFraTpsS302 response = fraTps.getTpsSvar().getHendelseDataS302().getRespons();

            RsTyperOgKilderResponse typerOgKilderResponse = new RsTyperOgKilderResponse();
            if (nonNull(response.getOversiktKilde())) {
                response.getOversiktHendelse().getEnoversiktHendelse().forEach(hendelseType ->
                        typerOgKilderResponse.getTyper().add(TypeOgAntall.builder()
                                .antall(Long.valueOf(hendelseType.getOversiktMeldingTypeAntall()))
                                .type(hendelseType.getOversiktMeldingType())
                                .build())
                );
            }
            if (nonNull(response.getOversiktKilde())) {
                response.getOversiktKilde().getEnoversiktKilde().forEach(kildeType ->
                        typerOgKilderResponse.getKilder().add(TypeOgAntall.builder()
                                .antall(Long.valueOf(kildeType.getOversiktKildeSystemAntall()))
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
            HendelsedataFraTpsS302 response = fraTps.getTpsSvar().getHendelseDataS302().getRespons();

            AtomicLong index = new AtomicLong(Long.valueOf(extractBuffersize(buffer)) *
                    (isNotBlank(response.getMeldingNummer()) ? Long.valueOf(response.getMeldingNummer()) - 1 : 0));
            return RsMeldingerResponse.builder()
                    .antallTotalt(isNotBlank(response.getMeldingerTotalt()) ? Long.valueOf(response.getMeldingerTotalt()) : 0)
                    .buffernumber(isNotBlank(response.getMeldingNummer()) ? Long.valueOf(response.getMeldingNummer()) : null)
                    .buffersize(Long.valueOf(extractBuffersize(buffer)))
                    .meldinger(response.getMelding().getEnmelding()
                            .stream()
                            .map(type -> RsMelding.builder()
                                    .index(index.getAndIncrement() + 1)
                                    .meldingNummer(type.getMNr())
                                    .tidspunkt(nonNull(type.getMTd()) ? convertToTimestamp(type.getMTd()) : null)
                                    .hendelseType(type.getMTp())
                                    .systemkilde(type.getMKs())
                                    .ident(type.getMFnr())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();

        } else {

            throw new NotFoundException(String.format(NO_DATA, miljoe, unpackPeriode(periode)));
        }
    }

    private String unpackPeriode(String periode) {
        return nonNull(periode) ? periode.replaceAll("\\$", " til ") : "";
    }

    @Async
    public void sendTilTps(RsAvspillerRequest request) {

        TpsPersonData tpsPersonData = mapperFacade.map(request, TpsPersonData.class);
        tpsPersonData.getTpsServiceRutine().setTypeOppslag(TypeOppslag.L.name());
        Long pageNumber = 0L;

        TpsPersonData personListe = null;
        do {
            tpsPersonData.getTpsServiceRutine().setSideNummer(Long.toString(++pageNumber));
            personListe = tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(tpsPersonData, request.getMiljoeFra());
            personListe.getTpsSvar().getHendelseDataS302().getRespons().getMelding().getEnmelding().forEach(melding -> {

                TpsPersonData detaljertMelding = getDetaljertMelding(request, melding.getMNr());

                String status;
                if (request.getFormat() == Meldingsformat.Ajourholdsmelding) {
                    status = tpsDistribusjonsmeldingService.sendDetailedSkdMessageToTps(
                            detaljertMelding.getTpsSvar().getHendelseDataS302().getRespons().getMeldingDetalj(),
                            request.getMiljoeTil(), request.getQueue());
                } else {
                    status = tpsDistribusjonsmeldingService.sendDetailedDistribusjonMessage(
                            detaljertMelding.getTpsSvar().getHendelseDataS302().getRespons().getMeldingDetalj(),
                            request.getMiljoeTil(), request.getQueue());
                }
                log.info(status);
            });
        }
        while ("S302006I".equals(personListe.getTpsSvar().getSvarStatus().getReturMelding()));
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

    private static TpsServiceRutineType.MeldingType convertMeldingType(List<String> meldingstyper) {
        TpsServiceRutineType.MeldingType meldingType = new TpsServiceRutineType.MeldingType();
        meldingType.getEnMeldingType().addAll(meldingstyper);
        return meldingType;
    }

    private static TpsServiceRutineType.KildeSystem convertKildeSystem(List<String> kilder) {
        TpsServiceRutineType.KildeSystem kildeSystem = new TpsServiceRutineType.KildeSystem();
        kildeSystem.getEtKildeSystem().addAll(kilder);
        return kildeSystem;
    }

    private static TpsServiceRutineType.Fnr convertIdenter(List<String> identer) {
        TpsServiceRutineType.Fnr fnr = new TpsServiceRutineType.Fnr();
        fnr.getEtFnr().addAll(identer);
        return fnr;
    }

    private static LocalDateTime convertToTimestamp(String timestamp) {
        return nonNull(timestamp) ?
                timestamp.length() > 10 ?
                        LocalDateTime.parse(timestamp) :
                        LocalDate.parse(timestamp).atStartOfDay() :
                null;
    }

    private static String extractDateFrom(String periode) {
        return nonNull(periode) ? LocalDateTime.parse(periode.split("\\$")[0]).toLocalDate().toString() : null;
    }

    private static String extractDateTo(String periode) {
        return nonNull(periode) && periode.split("\\$").length > 1 ?
                LocalDateTime.parse(periode.split("\\$")[1]).toLocalDate().toString() : null;
    }

    private static String extractTimeFrom(String periode) {
        return nonNull(periode) ? LocalDateTime.parse(periode.split("\\$")[0]).format(ofPattern(TIME_PATTERN)) : null;
    }

    private static String extractTimeeTo(String periode) {
        return nonNull(periode) && periode.split("\\$").length > 1 ?
                LocalDateTime.parse(periode.split("\\$")[1]).format(ofPattern(TIME_PATTERN)) : null;
    }

    private static List extractList(String commaSeparatedList) {
        return nonNull(commaSeparatedList) ? newArrayList(commaSeparatedList.split(",")) : new ArrayList();
    }

    private static String extractBuffernumber(String buffer) {
        return nonNull(buffer) ? Integer.toString(Integer.valueOf(buffer.split("\\$")[0]) + 1) : "1";
    }

    private static String extractBuffersize(String buffer) {
        return nonNull(buffer) && buffer.split("\\$").length > 1 ? (buffer.split("\\$")[1]) : "300";
    }

    public List<RsAvspillerProgress> getStatuser(Long bestillingId) {
        return null;
    }
}
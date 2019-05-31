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
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.Meldingsformat;
import no.nav.tps.forvalteren.domain.rs.RsMelding;
import no.nav.tps.forvalteren.domain.rs.RsMeldingerResponse;
import no.nav.tps.forvalteren.domain.rs.RsTyperOgKilderResponse;
import no.nav.tps.forvalteren.domain.rs.TypeOgAntall;
import no.nav.tps.forvalteren.domain.rs.TypeOppslag;
import no.nav.tps.forvalteren.domain.rs.skd.RsAvspillerProgress;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsDistribusjonsmeldingService;
import no.nav.tps.xjc.ctg.domain.s302.HendelsedataFraTpsS302;
import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;
import no.nav.tps.xjc.ctg.domain.s302.TpsServiceRutineType;

@Service
public class AvspillerService {

    @Autowired
    private TpsDistribusjonsmeldingService tpsDistribusjonsmeldingService;

    public List<RsAvspillerProgress> getStatuser(Long bestillingId) {

        return null;
    }

    public RsTyperOgKilderResponse getTyperOgKilder(Meldingsformat format, String periode, String miljoe) {

        TpsPersonData fraTps = getHendelsedataFraTps(miljoe, periode, format, null, null, null, null);

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
    }

    public RsMeldingerResponse getMeldinger(String miljoe, String periode, Meldingsformat format, String meldingstyper, String kilder, String identer, String buffer) {

        TpsPersonData fraTps = getHendelsedataFraTps(miljoe, periode, format, meldingstyper, kilder, identer, buffer);

        HendelsedataFraTpsS302 response = fraTps.getTpsSvar().getHendelseDataS302().getRespons();

        return RsMeldingerResponse.builder()
                .antallTotalt(isNotBlank(response.getMeldingerTotalt()) ? Long.valueOf(response.getMeldingerTotalt()) : 0)
                .buffernumber(isNotBlank(response.getMeldingNummer()) ? Long.valueOf(response.getMeldingNummer()) : null)
                .meldinger(response.getMelding().getEnmelding()
                        .stream()
                        .map(type -> RsMelding.builder()
                                .meldingNummer(type.getMNr())
                                .tidspunkt(nonNull(type.getMTd()) ? convertToTimestamp(type.getMTd()) : null)
                                .hendelseType(type.getMTp())
                                .systemkilde(type.getMKs())
                                .ident(type.getMFnr())
                                .build())
                        .collect(Collectors.toList()))
                .build();
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
                tpsServiceRutineType.setMeldingType(convertMeldingType(meldingstyper));
                tpsServiceRutineType.setKildeSystem(convertKildeSystem(kilder));
                tpsServiceRutineType.setFnr(convertIdenter(identer));

        TpsPersonData tpsPersonData = new TpsPersonData();
                tpsPersonData.setTpsServiceRutine(tpsServiceRutineType);
        return tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(tpsPersonData, miljoe);
    }

    private static TpsServiceRutineType.MeldingType convertMeldingType(String meldingstyper) {
        TpsServiceRutineType.MeldingType meldingType = new TpsServiceRutineType.MeldingType();
        meldingType.getEnMeldingType().addAll(extractList(meldingstyper));
        return meldingType;
    }

    private static TpsServiceRutineType.KildeSystem convertKildeSystem(String kilder) {
        TpsServiceRutineType.KildeSystem kildeSystem = new TpsServiceRutineType.KildeSystem();
        kildeSystem.getEtKildeSystem().addAll(extractList(kilder));
        return kildeSystem;
    }

    private static TpsServiceRutineType.Fnr convertIdenter(String identer) {
        TpsServiceRutineType.Fnr fnr = new TpsServiceRutineType.Fnr();
        fnr.getEtFnr().addAll(extractList(identer));
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
        return nonNull(periode) ? LocalDateTime.parse(periode.split("\\$")[0]).format(ofPattern("HH:mm:ss")) : null;
    }

    private static String extractTimeeTo(String periode) {
        return nonNull(periode) && periode.split("\\$").length > 1 ?
                LocalDateTime.parse(periode.split("\\$")[1]).format(ofPattern("HH:mm:ss")) : null;
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
}
package no.nav.tps.forvalteren.service.command.avspiller;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Objects.nonNull;
import static org.assertj.core.util.Lists.newArrayList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import no.nav.tps.xjc.ctg.domain.s302.TpsServiceRutineType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AvspillerConvertUtils {

    private static final String TIME_PATTERN = "HH:mm:ss";

    public static TpsServiceRutineType.MeldingType convertMeldingType(List<String> meldingstyper) {
        TpsServiceRutineType.MeldingType meldingType = new TpsServiceRutineType.MeldingType();
        meldingType.getEnMeldingType().addAll(meldingstyper);
        return meldingType;
    }

    public static TpsServiceRutineType.KildeSystem convertKildeSystem(List<String> kilder) {
        TpsServiceRutineType.KildeSystem kildeSystem = new TpsServiceRutineType.KildeSystem();
        kildeSystem.getEtKildeSystem().addAll(kilder);
        return kildeSystem;
    }

    public static TpsServiceRutineType.Fnr convertIdenter(List<String> identer) {
        TpsServiceRutineType.Fnr fnr = new TpsServiceRutineType.Fnr();
        fnr.getEtFnr().addAll(identer);
        return fnr;
    }

    public static LocalDateTime convertToTimestamp(String timestamp) {
        return nonNull(timestamp) ?
                timestamp.length() > 10 ?
                        LocalDateTime.parse(timestamp) :
                        LocalDate.parse(timestamp).atStartOfDay() :
                null;
    }

    public static String extractDateFrom(String periode) {
        return nonNull(periode) ? LocalDateTime.parse(periode.split("\\$")[0]).toLocalDate().toString() : null;
    }

    public static String extractDateTo(String periode) {
        return nonNull(periode) && periode.split("\\$").length > 1 ?
                LocalDateTime.parse(periode.split("\\$")[1]).toLocalDate().toString() : null;
    }

    public static String extractTimeFrom(String periode) {
        return nonNull(periode) ? LocalDateTime.parse(periode.split("\\$")[0]).format(ofPattern(TIME_PATTERN)) : null;
    }

    public static String extractTimeeTo(String periode) {
        return nonNull(periode) && periode.split("\\$").length > 1 ?
                LocalDateTime.parse(periode.split("\\$")[1]).format(ofPattern(TIME_PATTERN)) : null;
    }

    public static List extractList(String commaSeparatedList) {
        return nonNull(commaSeparatedList) ? newArrayList(commaSeparatedList.split(",")) : new ArrayList();
    }

    public static String extractBuffernumber(String buffer) {
        return nonNull(buffer) ? Integer.toString(Integer.valueOf(buffer.split("\\$")[0]) + 1) : "1";
    }

    public static String extractBuffersize(String buffer) {
        return nonNull(buffer) && buffer.split("\\$").length > 1 ? (buffer.split("\\$")[1]) : "150";
    }

    public static String unpackPeriode(String periode) {
        return nonNull(periode) ? periode.replaceAll("\\$", " til ") : "";
    }
}

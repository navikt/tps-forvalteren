package no.nav.tps.forvalteren.service.command.testdata.utils;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

public final class ExtractErrorStatus {

    private ExtractErrorStatus() {

    }

    public static String extract(String status) {
        if (nonNull(status) && status.contains("FEIL")) {
            return status;
        } else if (nonNull(status) && status.length() > 3) {
            return format("FEIL: %s", status.substring(3).replaceAll("\\d*%[A-Z]\\d*%","").replaceAll("%; *", ""));
        } else {
            return "FEIL: UKJENT";
        }
    }
}
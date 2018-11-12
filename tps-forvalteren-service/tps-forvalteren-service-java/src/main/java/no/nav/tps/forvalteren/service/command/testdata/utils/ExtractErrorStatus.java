package no.nav.tps.forvalteren.service.command.testdata.utils;

import static java.lang.String.format;

public final class ExtractErrorStatus {

    private ExtractErrorStatus() {

    }

    public static String extract(String status) {
        if (status != null && status.contains("FEIL")) {
            return status;
        } else if (status != null && status.length() > 3) {
            return format("FEIL: %s", status.substring(3));
        } else {
            return "FEIL: UKJENT";
        }
    }
}
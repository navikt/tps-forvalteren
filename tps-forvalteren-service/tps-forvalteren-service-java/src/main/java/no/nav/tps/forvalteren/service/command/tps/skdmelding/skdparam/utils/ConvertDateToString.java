package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConvertDateToString {

    private ConvertDateToString() {
    }

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HHmmss");
    private static final DateTimeFormatter DATE2_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String yyyyMMdd(LocalDateTime localDateTime) {

        return localDateTime != null ? localDateTime.format(DATE_FORMAT) : null;
    }

    public static String hhMMss(LocalDateTime localDateTime) {

        return localDateTime != null ? localDateTime.format(TIME_FORMAT) : null;
    }

    public static String yyyy_MM_dd(LocalDateTime localDateTime) {

        return localDateTime != null ? localDateTime.format(DATE2_FORMAT) : null;
    }
}

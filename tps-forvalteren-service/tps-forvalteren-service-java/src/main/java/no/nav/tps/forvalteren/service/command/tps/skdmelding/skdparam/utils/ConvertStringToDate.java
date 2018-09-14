package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ConvertStringToDate {

    private ConvertStringToDate() {
    }

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HHmmss");
    private static final DateTimeFormatter DATE2_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDateTime yyyyMMdd(String date) {

        return date != null ? LocalDate.parse(date, DATE_FORMAT).atStartOfDay() : null;
    }

    public static LocalDateTime hhMMss(String date) {

        return date != null ? LocalDate.parse(date, TIME_FORMAT).atStartOfDay() : null;
    }

    public static LocalDateTime yyyysMMsdd(String date) {

        return date != null ? LocalDate.parse(date, DATE2_FORMAT).atStartOfDay() : null;
    }
}
package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class ConvertStringToDate {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HHmmss");
    private static final DateTimeFormatter DATE2_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private ConvertStringToDate() {
    }

    public static LocalDateTime yyyyMMdd(String date) {

        return date != null ? LocalDate.parse(date, DATE_FORMAT).atStartOfDay() : null;
    }

    public static LocalTime hhMMss(String date) {

        return date != null ? LocalTime.parse(date, TIME_FORMAT) : null;
    }

    public static LocalDateTime yyyysMMsdd(String date) {

        return date != null ? LocalDate.parse(date, DATE2_FORMAT).atStartOfDay() : null;
    }
}
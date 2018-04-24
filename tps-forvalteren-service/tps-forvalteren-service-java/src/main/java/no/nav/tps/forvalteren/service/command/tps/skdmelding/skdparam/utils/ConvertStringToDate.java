package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConvertStringToDate {

    public static LocalDateTime yyyyMMdd(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6));

        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.NOON);
        return localDateTime;
    }
}

package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import java.time.LocalDateTime;

public class GetStringVersionOfLocalDateTime {

    public static String yyyyMMdd(LocalDateTime localDateTime){
        String year = String.valueOf(localDateTime.getYear());
        String month = formaterDato(localDateTime.getMonthValue());
        String day = formaterDato(localDateTime.getDayOfMonth());

        return year + month + day;
    }

    public static String hhMMss(LocalDateTime localDateTime){
        String hour = formaterDato(localDateTime.getHour());
        String minute = formaterDato(localDateTime.getMinute());
        String second = formaterDato(localDateTime.getSecond());

        return hour + minute + second;
    }

    private static String formaterDato(int tid) {
        if (tid < 10) {
            return "0" + tid;
        }
        return String.valueOf(tid);
    }
}

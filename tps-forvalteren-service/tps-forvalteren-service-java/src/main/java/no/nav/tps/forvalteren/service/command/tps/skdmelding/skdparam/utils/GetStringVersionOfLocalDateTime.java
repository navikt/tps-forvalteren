package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetStringVersionOfLocalDateTime {

    public static String yyyyMMdd(LocalDateTime localDateTime){
        DateTimeFormatter dateFormat =  DateTimeFormatter.ofPattern("yyyyMMdd");
        return localDateTime.format(dateFormat);
    }

    public static String hhMMss(LocalDateTime localDateTime){
        DateTimeFormatter timeFormat =  DateTimeFormatter.ofPattern("hhmmss");
        return localDateTime.format(timeFormat);
    }

}

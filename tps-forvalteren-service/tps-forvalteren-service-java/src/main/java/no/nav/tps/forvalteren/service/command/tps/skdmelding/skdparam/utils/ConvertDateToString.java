package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConvertDateToString {

    public static String yyyyMMdd(LocalDateTime localDateTime){
        DateTimeFormatter dateFormat =  DateTimeFormatter.ofPattern("yyyyMMdd");
        return localDateTime.format(dateFormat);
    }

    public static String yyyyMMdd(LocalDate localDate){
        DateTimeFormatter dateFormat =  DateTimeFormatter.ofPattern("yyyyMMdd");
        return localDate.format(dateFormat);
    }

    public static String hhMMss(LocalDateTime localDateTime){
        DateTimeFormatter timeFormat =  DateTimeFormatter.ofPattern("HHmmss");
        return localDateTime.format(timeFormat);
    }

}

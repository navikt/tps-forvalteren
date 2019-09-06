package no.nav.tps.forvalteren.service.command.testdata.utils;

import static java.lang.Integer.parseInt;
import static java.time.LocalDateTime.of;
import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 * INDIVID(POS 7-9) 500-749 OG ÅR > 54 => ÅRHUNDRE = 1800
 * INDIVID(POS 7-9) 000-499            => ÅRHUNDRE = 1900
 * INDIVID(POS 7-9) 900-999 OG ÅR > 39 => ÅRHUNDRE = 1900
 * INDIVID(POS 7-9) 500-999 OG ÅR < 40 => ÅRHUNDRE = 2000
 */
@Service
public class HentDatoFraIdentService {

    private static final LocalDateTime TPS_MIN_REG_DATE = of(1900,1,1,0,0);

    public LocalDateTime extract(String ident) {

        int year = parseInt(ident.substring(4, 6));
        int individ = parseInt(ident.substring(6, 9));

        // Find century
        int century;
        if (individ < 500 || (individ >= 900 && year > 39)) {
            century = 1900;
        } else if (individ >= 500 && year < 40) {
            century = 2000;
        } else if (individ >= 500 && individ < 750 && year > 54) {
            century = 1800;
        } else {
            century = 2000;
        }

        return LocalDateTime.of(century + year, getMonth(ident), getDay(ident), 0, 0);
    }

    public static LocalDateTime enforceValidTpsDate(LocalDateTime date) {
        if (isNull(date)) {
            return null;
        } else {
            return date.isBefore(TPS_MIN_REG_DATE) ? TPS_MIN_REG_DATE : date;
        }
    }

    private int getDay(String ident) {
        // Fix D-number
        return ident.charAt(0) >= '4' ? parseInt(ident.substring(0, 2)) - 40 :
                parseInt(ident.substring(0, 2));
    }

    private int getMonth(String ident) {
        // Fix B-number
        return ident.charAt(2) >= '2' ? parseInt(ident.substring(2, 4)) - 20 :
                parseInt(ident.substring(2, 4));
    }
}
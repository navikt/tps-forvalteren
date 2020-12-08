package no.nav.tps.forvalteren.service.command.testdata.utils;

import static java.lang.Integer.parseInt;
import static java.time.LocalDateTime.of;
import static java.util.Objects.isNull;

import java.time.LocalDate;
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

    private static final LocalDateTime TPS_MIN_REG_DATE = of(1900, 1, 1, 0, 0);

    public LocalDateTime extract(String ident) {

        int year = parseInt(ident.substring(4, 6));
        int individ = parseInt(ident.substring(6, 9));

        // Find century
        int century;
        if (parseInt(ident.substring(6, 10)) == 0) {
            century = year <= LocalDate.now().getYear() % 100 ? 2000 : 1900;
        } else if (individ < 500 || (individ >= 900 && year > 39)) {
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

        char monthId = ident.charAt(2);
        int month = parseInt(ident.substring(2, 4));

        // Tenor-syntetic FNR/DNR
        if (month >= '8') {
            return month - 80;

        // NAV-syntetic BOST
        } else if (monthId >= '6') {
            return month - 60;

        // NAV-syntetic FNR/DNR
        } else if (monthId >= '4') {
            return month - 40;

        // BOST
        } else if (monthId >= '2') {
            return month - 20;

        // FNR/DNR
        } else {
            return month;
        }
    }
}
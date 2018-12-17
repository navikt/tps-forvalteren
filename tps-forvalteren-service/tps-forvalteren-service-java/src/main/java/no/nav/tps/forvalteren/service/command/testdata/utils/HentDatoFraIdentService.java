package no.nav.tps.forvalteren.service.command.testdata.utils;

import static java.lang.Integer.parseInt;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 * Simplified unambiguous interpretation
 * INDIVID(POS 7-9) 000-499            => ÅRHUNDRE = 1900
 * INDIVID(POS 7-9) 500-999 OG ÅR < 40 => ÅRHUNDRE = 2000
 */
@Service
public class HentDatoFraIdentService {

    public LocalDateTime extract(String ident) {

        int year = parseInt(ident.substring(4, 6));
        int individ = parseInt(ident.substring(6, 9));

        // Determine century
        int century = individ < 500 ? 1900 : 2000;

        return LocalDateTime.of(century + year, getMonth(ident), getDay(ident), 0, 0);
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
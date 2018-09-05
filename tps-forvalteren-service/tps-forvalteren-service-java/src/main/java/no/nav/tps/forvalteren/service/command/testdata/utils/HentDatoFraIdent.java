package no.nav.tps.forvalteren.service.command.testdata.utils;

import static java.lang.Integer.parseInt;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class HentDatoFraIdent {

    public LocalDateTime extract(String ident) {
        // Fix D-number
        int day = ident.charAt(0) >= '4' ? parseInt(ident.substring(0, 2)) - 40 :
                parseInt(ident.substring(0, 2));
        // Fix B-number
        int month = ident.charAt(2) >= '2' ? parseInt(ident.substring(2, 4)) - 20 :
                parseInt(ident.substring(2, 4));
        // Find century
        int century = parseInt(ident.substring(6, 9)) < 500 ||
                parseInt(ident.substring(6, 9)) >= 900 ? 1900 : 2000;

        return LocalDateTime.of(century + parseInt(ident.substring(4, 6)), month, day, 0, 0);
    }
}
package no.nav.tps.forvalteren.service.command.testdata;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SjekkOmGyldigeIdenter {

    private static final int[] KONTROLL_SIFFER_C1 = { 3, 7, 6, 1, 8, 9, 4, 5, 2 };
    private static final int[] KONTROLL_SIFFER_C2 = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };

    public Set<String> execute(Set<String> identListe) {
        Set<String> gyldigeIdenter = new HashSet<>();
        for (String ident : identListe) {
            if (ident.length() == 11 &&  isValidFnrDnrOrBnr(ident) && isKontrollsifferValid(ident)) {
                gyldigeIdenter.add(ident);
            }
        }
        return gyldigeIdenter;
    }

    private boolean isValidFnrDnrOrBnr(String ident) {
        int day = Integer.parseInt(ident.substring(0, 2));
        int month = Integer.parseInt(ident.substring(2, 4));
        return isFnr(day, month) || isDnr(day, month) || isBnr(day, month);
    }

    private boolean isBnr(int day, int month) {
        return day > 0 && day < 32 && month > 20 && month < 33;
    }

    private boolean isDnr(int day, int month) {
        return day > 40 && day < 72 && month > 0 && month < 13;
    }

    private boolean isFnr(int day, int month) {
        return day > 0 && day < 32 && month > 0 && month < 13;
    }

    private boolean isKontrollsifferValid(String ident) {
        int kontrollsiffer1 = calculateKontrollsiffer(ident, true, KONTROLL_SIFFER_C1);
        int kontrollsiffer2 = calculateKontrollsiffer(ident, false, KONTROLL_SIFFER_C2);

        String korrektKontrollsiffer = String.format("%d%d", kontrollsiffer1, kontrollsiffer2);
        String kontrollsifferIIdent = ident.substring(9, 11);
        return korrektKontrollsiffer.equals(kontrollsifferIIdent);
    }

    private int calculateKontrollsiffer(String ident, boolean isFirstKontrollsiffer, int... multipliers) {
        int kontrollsiffer = 0;
        int skipEnd = isFirstKontrollsiffer ? 2 : 1;
        for (int index = 0; index < ident.length() - skipEnd; index++) {
            int number = Character.getNumericValue(ident.charAt(index));
            kontrollsiffer += number * multipliers[index];
        }
        kontrollsiffer = Math.abs((kontrollsiffer % 11) - 11);

        return kontrollsiffer == 11 ? 0 : kontrollsiffer;
    }

}

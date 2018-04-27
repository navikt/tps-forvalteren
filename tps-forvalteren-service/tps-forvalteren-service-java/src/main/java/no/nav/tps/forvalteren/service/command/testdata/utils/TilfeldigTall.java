package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.security.SecureRandom;

public class TilfeldigTall {
    public static String  tilfeldigTall(String min, String max) {
        Integer minimum = Integer.parseInt(min);
        return String.format("%03d", new SecureRandom().nextInt(Integer.parseInt(max)-minimum + 1) + minimum);
    }
}

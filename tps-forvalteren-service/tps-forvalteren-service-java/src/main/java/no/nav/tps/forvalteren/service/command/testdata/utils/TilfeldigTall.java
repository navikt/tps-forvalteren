package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.util.concurrent.ThreadLocalRandom;

public class TilfeldigTall {
    public static String  tilfeldigTall(String min, String max) {
        return String.format("%03d", ThreadLocalRandom.current().nextInt( Integer.parseInt(min), Integer.parseInt(max)));
    }
}

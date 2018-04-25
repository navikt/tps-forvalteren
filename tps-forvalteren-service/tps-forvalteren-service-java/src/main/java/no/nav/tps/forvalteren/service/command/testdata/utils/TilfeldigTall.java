package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.util.Random;

public class TilfeldigTall {
    public static String  tilfeldigTall(String min, String max) {
        return String.format("%03d",  new Random().nextInt(Integer.parseInt(min)) + Integer.parseInt(max));
    }
}

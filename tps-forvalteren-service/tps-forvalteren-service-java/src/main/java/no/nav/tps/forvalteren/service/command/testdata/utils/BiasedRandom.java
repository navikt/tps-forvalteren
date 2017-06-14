package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.util.concurrent.ThreadLocalRandom;

public class BiasedRandom {

    /**
     * Lager en topp eller bunntung random. Hvis Bias > 1 så blir den bunntung. Bias < 1 blir topptung,
     * */
    public static int lagBiasedRandom(int low, int high, float bias){
        float biasedRandom = ThreadLocalRandom.current().nextFloat();
        double biasedRandomD = Math.pow(biasedRandom, bias);
        return (int) (low + (high - low)* biasedRandomD);
    }

    public static int lagTopptungRandom(int low, int high){
        return lagBiasedRandom(low, high, 0.3F);
    }

    public static int lagBunntungRandom(int low, int high){
        return lagBiasedRandom(low, high, 3.0F);
    }
}

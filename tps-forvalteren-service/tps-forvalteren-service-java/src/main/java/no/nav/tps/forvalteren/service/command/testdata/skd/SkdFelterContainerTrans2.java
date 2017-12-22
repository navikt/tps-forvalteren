package no.nav.tps.forvalteren.service.command.testdata.skd;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SkdFelterContainerTrans2 implements SkdFelterContainer{

    private static final String WHITESPACE_5_STK = "     ";
    private static final String WHITESPACE_10_STK = WHITESPACE_5_STK + WHITESPACE_5_STK;
    private static final String WHITESPACE_25_STK = WHITESPACE_5_STK + WHITESPACE_10_STK + WHITESPACE_10_STK;
    private static final String WHITESPACE_40_STK = WHITESPACE_10_STK + WHITESPACE_10_STK + WHITESPACE_10_STK + WHITESPACE_10_STK;
    private static final String WHITESPACE_30_STK = WHITESPACE_5_STK + WHITESPACE_25_STK;
    private static final String WHITESPACE_50_STK = WHITESPACE_25_STK + WHITESPACE_25_STK;
    private static final String WHITESPACE_100_STK = WHITESPACE_50_STK + WHITESPACE_50_STK;
    private static final String WHITESPACE_200_STK = WHITESPACE_100_STK + WHITESPACE_100_STK;
    private static final String WHITESPACE_400_STK = WHITESPACE_200_STK + WHITESPACE_200_STK;
    private static final String WHITESPACE_583_STK = WHITESPACE_400_STK + WHITESPACE_100_STK + WHITESPACE_50_STK + WHITESPACE_30_STK + "   ";

    public List<SkdFeltDefinisjon> hentSkdFelter() {
        ArrayList<SkdFeltDefinisjon> skdFelter = new ArrayList<>();
        leggTilSkdFeltDefinisjoner(skdFelter);
        return skdFelter;
    }

    private void leggTilSkdFeltDefinisjoner(ArrayList<SkdFeltDefinisjon> skdFelter) {
        int rekkefoelge = 1;
        skdFelter.add(new SkdFeltDefinisjon("fodselsnr", "00000000000", rekkefoelge++, 11,1,11));
        skdFelter.add(new SkdFeltDefinisjon("maskindato", "00000000", rekkefoelge++, 8,12,19));
        skdFelter.add(new SkdFeltDefinisjon("maskintid", "000000", rekkefoelge++, 6,20,25));
        skdFelter.add(new SkdFeltDefinisjon("transtype", "0", rekkefoelge++, 1,26,26));
        skdFelter.add(new SkdFeltDefinisjon("aarsakskode", "00", rekkefoelge++, 2,27,28));
        skdFelter.add(new SkdFeltDefinisjon("FILLER", WHITESPACE_30_STK + "   ", rekkefoelge++, 33,29,61));

        int startPosition;
        int endPosition = 61;
        for(int counter = 1; counter < 14; counter++) {
            startPosition = endPosition + 1;
            endPosition += 6;
            skdFelter.add(new SkdFeltDefinisjon("barnFodsdato" + counter, "000000", rekkefoelge++, 6, startPosition, endPosition));

            startPosition = endPosition + 1;
            endPosition += 5;
            skdFelter.add(new SkdFeltDefinisjon("barnPersnr" + counter, "00000", rekkefoelge++, 5, startPosition, endPosition));

            startPosition = endPosition + 1;
            endPosition += 50;
            skdFelter.add(new SkdFeltDefinisjon("barnNavn" + counter, WHITESPACE_50_STK, rekkefoelge++, 50, startPosition, endPosition));

            startPosition = endPosition + 1;
            endPosition += 1;
            skdFelter.add(new SkdFeltDefinisjon("barnKjoenn" + counter, " ", rekkefoelge++, 1, startPosition, endPosition));
        }

        skdFelter.add(new SkdFeltDefinisjon("FILLER", WHITESPACE_40_STK + "    ", rekkefoelge++, 44,868,911));
        skdFelter.add(new SkdFeltDefinisjon("sekvensnr", "000000", rekkefoelge++, 6,912,917));
        skdFelter.add(new SkdFeltDefinisjon("FILLER", WHITESPACE_583_STK, rekkefoelge, 583,918,1500));

    }

}
package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.service.command.testdata.skd.impl.WhitespaceConstants.DUMMY_DATO;
import static no.nav.tps.forvalteren.service.command.testdata.skd.impl.WhitespaceConstants.DUMMY_IDENT;
import static no.nav.tps.forvalteren.service.command.testdata.skd.impl.WhitespaceConstants.SIX_OES;
import static no.nav.tps.forvalteren.service.command.testdata.skd.impl.WhitespaceConstants.WHITESPACE_30_STK;
import static no.nav.tps.forvalteren.service.command.testdata.skd.impl.WhitespaceConstants.WHITESPACE_500_STK;
import static no.nav.tps.forvalteren.service.command.testdata.skd.impl.WhitespaceConstants.WHITESPACE_50_STK;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SkdFelterContainerTrans2 implements SkdFelterContainer {

    private static final String FILLER = "filler";

    public List<SkdFeltDefinisjon> hentSkdFelter() {
        ArrayList<SkdFeltDefinisjon> skdFelter = new ArrayList<>();
        leggTilSkdFeltDefinisjoner(skdFelter);
        return skdFelter;
    }

    private void leggTilSkdFeltDefinisjoner(ArrayList<SkdFeltDefinisjon> skdFelter) {
        int rekkefoelge = 1;
        skdFelter.add(new SkdFeltDefinisjon("fodselsnr", DUMMY_IDENT, rekkefoelge++, 11, 1, 11));
        skdFelter.add(new SkdFeltDefinisjon("maskindato", DUMMY_DATO, rekkefoelge++, 8, 12, 19));
        skdFelter.add(new SkdFeltDefinisjon("maskintid", SIX_OES, rekkefoelge++, 6, 20, 25));
        skdFelter.add(new SkdFeltDefinisjon("transtype", "0", rekkefoelge++, 1, 26, 26));
        skdFelter.add(new SkdFeltDefinisjon("aarsakskode", "00", rekkefoelge++, 2, 27, 28));
        skdFelter.add(new SkdFeltDefinisjon(FILLER, WHITESPACE_30_STK + "   ", rekkefoelge++, 33, 29, 61));

        int startPosition;
        int endPosition = 61;
        for (int counter = 1; counter < 14; counter++) {
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

        skdFelter.add(new SkdFeltDefinisjon(FILLER, "00000000000000000000000000000000000000000000", rekkefoelge++, 44, 868, 911));
        skdFelter.add(new SkdFeltDefinisjon("sekvensnr", SIX_OES, rekkefoelge++, 6, 912, 917));
        skdFelter.add(new SkdFeltDefinisjon(FILLER, WHITESPACE_500_STK + WHITESPACE_50_STK + WHITESPACE_30_STK + "   ", rekkefoelge, 583, 918, 1500));
    }
}
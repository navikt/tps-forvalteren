package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.service.command.testdata.utils.BiasedRandom;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BiasedRandomTest {

    private static final int loops = 1000000;


    @Test
    public void lagerKunTallInnenRangeGittNummerRange() throws Exception {
        for(int i=0; i<loops; i++){
            int randomTall = BiasedRandom.lagBiasedRandom(0,499, 3.0f);
            assertTrue(randomTall >= 0 && randomTall <= 499);
        }
    }


    /*
    Teknisk sett kan testene under feile hvis man har veldig uflaks med randoms,
    men med så mnage loops bør det være lite tilfeldighet.
    */
    @Test
    public void topptungRandomErTopptung() throws Exception {
        int sum = 0;
        for(int i=0; i<loops; i++){
            int randomTall = BiasedRandom.lagTopptungRandom(0,499);
            sum = sum + randomTall;
        }
        assertTrue((sum/loops) > 250);
    }

    @Test
    public void bunntungErRandomBunntung() throws Exception {
        int sum = 0;
        for(int i=0; i<loops; i++){
            int randomTall = BiasedRandom.lagBunntungRandom(0,499 );
            sum = sum + randomTall;
        }
        assertTrue((sum/loops) < 250);
    }
}
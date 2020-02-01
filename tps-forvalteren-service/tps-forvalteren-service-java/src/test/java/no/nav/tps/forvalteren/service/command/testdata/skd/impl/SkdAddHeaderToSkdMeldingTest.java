package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;

@RunWith(MockitoJUnitRunner.class)
public class SkdAddHeaderToSkdMeldingTest {

    private static final int FASIT_LENGTH_HEADER = 46;

    private static final int INDEX_START_TILDELINGSKODE = 873;
    private static final int INDEX_SLUTT_TILDELINGSKODE = 874;

    private static final int INDEX_START_AARSAKSKODE = 26;
    private static final int INDEX_SLUTT_AARSAKSKODE = 28;

    private static final int INDEX_START_TRANSTYPE = 25;
    private static final int INDEX_SLUTT_TRANSTYPE = 26;

    private static final int INDEX_START_AARSAKSKODE_HEADER = 22;
    private static final int INDEX_START_TRANSTYPE_HEADER = 24;
    private static final int INDEX_START_TILDELINGSKODE_HEADER = 25;

    private static final String DEFUALT_TRANSTYPE = "1";

    private String testSkdMld = "060121241102017062700000010220170627100000000Saks                                              Rask                                                                                                                                                                           00000000                        000000000000809674014020170627100000000010000000000000000000                                                  000201706272017062700000000000000000000                                                   00000000                                                                                          0000012017062720170627000000000000000000000000000000000000000 00000000 00000000 00000000 000000000000000000000000000                                                  00000000000000                                                  00000000000000000000000000000000000000000  2                0000        00000000 000000     000000000000        00000000                                                                                                                        00000000000 00000000 0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000 00000000                                                                                                                                                                                                                                                                                                         ";
    private StringBuilder skdMeldingSB = new StringBuilder(testSkdMld);

    @InjectMocks
    private SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;

    @Test
    public void headerIsAdded() {

        int sizeMld = testSkdMld.length();

        StringBuilder skdMedHeader = skdAddHeaderToSkdMelding.execute(skdMeldingSB);

        assertTrue(skdMedHeader.length() > sizeMld);
    }

    @Test
    public void henterRiktigAarsakskode() {
        String fasitAarsakskode = "02";
        String aarsakskode = testSkdMld.substring(INDEX_START_AARSAKSKODE, INDEX_SLUTT_AARSAKSKODE);

        assertEquals(aarsakskode, fasitAarsakskode);

        StringBuilder skdMedHeader = skdAddHeaderToSkdMelding.execute(skdMeldingSB);

        String skdMldMedHeader = skdMeldingSB.toString().substring(INDEX_START_AARSAKSKODE_HEADER, 24);

        assertEquals(skdMldMedHeader, fasitAarsakskode);
    }

    @Test
    public void henterRiktigTranstype() {
        String fasitAarsakskode = DEFUALT_TRANSTYPE;
        String aarsakskode = testSkdMld.substring(INDEX_START_TRANSTYPE, INDEX_SLUTT_TRANSTYPE);

        assertEquals(aarsakskode, fasitAarsakskode);

        StringBuilder skdMedHeader = skdAddHeaderToSkdMelding.execute(skdMeldingSB);

        String skdMldMedHeader = skdMedHeader.toString().substring(INDEX_START_TRANSTYPE_HEADER, 25);

        assertEquals(skdMldMedHeader, fasitAarsakskode);
    }

    @Test
    public void henterRiktigTildelingskode() {
        String fasitAarsakskode = "2";
        String aarsakskode = testSkdMld.substring(INDEX_START_TILDELINGSKODE, INDEX_SLUTT_TILDELINGSKODE);

        assertEquals(aarsakskode, fasitAarsakskode);

        StringBuilder skdMedHeader = skdAddHeaderToSkdMelding.execute(skdMeldingSB);

        String skdMldMedHeader = skdMedHeader.toString().substring(INDEX_START_TILDELINGSKODE_HEADER, 26);

        assertEquals(skdMldMedHeader, fasitAarsakskode);
    }

    @Test
    public void erDetRiktigLengdePaaHeader() {

        int sizeSkdMld = skdMeldingSB.length();

        StringBuilder skdMedHeader = skdAddHeaderToSkdMelding.execute(skdMeldingSB);

        int sizeSkdMldMedHeader = skdMedHeader.length();

        assertSame(FASIT_LENGTH_HEADER , (sizeSkdMldMedHeader - sizeSkdMld));
    }

}
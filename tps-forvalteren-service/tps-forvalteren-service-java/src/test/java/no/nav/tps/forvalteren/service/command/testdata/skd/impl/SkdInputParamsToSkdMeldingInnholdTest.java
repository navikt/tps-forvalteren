package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFeltDefinisjon;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdInputParamsToSkdMeldingInnhold;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SkdInputParamsToSkdMeldingInnholdTest {

    /* Alle SKD felter er bygd opp ved hjelp av disse reglene
    *  https://confluence.adeo.no/display/FEL/Recordbeskrivelse+i+en+SKD-trans.+Versjon+juni+2015
    * */

    private HashMap<String, String> skdInputMap = new HashMap<>();
    private List<SkdFeltDefinisjon> feltDefinisjoner = new ArrayList<>();
    private SkdFeltDefinisjon skdFeltDefinisjon1 = new SkdFeltDefinisjon();
    private SkdFeltDefinisjon skdFeltDefinisjon2 = new SkdFeltDefinisjon();
    private String DEFAULT_VERDI_SKD = "0000000000";

    @Mock
    private SkdFelterContainerTrans1 skdFelterContainerTrans1;

    @InjectMocks
    private SkdInputParamsToSkdMeldingInnhold skdInputParamsToSkdMeldingInnhold;

    @Before
    public void setup() {
        feltDefinisjoner.add(skdFeltDefinisjon1);
        feltDefinisjoner.add(skdFeltDefinisjon2);

        when(skdFelterContainerTrans1.hentSkdFelter()).thenReturn(feltDefinisjoner);
    }

    @Test
    public void hvisSkdFelteneIDefinisjonFinnesSaaSettesSomVerdiIStringen() {
        skdInputMap.put("testFelt1", "test1");
        skdInputMap.put("testFelt2", "test2");

        skdFeltDefinisjon1.setNokkelNavn("testFelt1");
        skdFeltDefinisjon2.setNokkelNavn("testFelt2");

        skdFeltDefinisjon1.setDefaultVerdi(DEFAULT_VERDI_SKD);
        skdFeltDefinisjon2.setDefaultVerdi(DEFAULT_VERDI_SKD);

        String finalSkdMelding = "test100000test200000";

        StringBuilder skdMldSB = skdInputParamsToSkdMeldingInnhold.execute(skdInputMap, skdFelterContainerTrans1);

        assertThat(finalSkdMelding, is(equalTo(skdMldSB.toString())));
    }

    @Test
    public void hvisSkdParameterenUtfyllerHeleGittePlassenSinIStringenSaaSerManIngenDefaultVerdi() {
        skdInputMap.put("testFelt1", "test1test1");
        skdInputMap.put("testFelt2", "test2test2");

        skdFeltDefinisjon1.setNokkelNavn("testFelt1");
        skdFeltDefinisjon2.setNokkelNavn("testFelt2");

        skdFeltDefinisjon1.setDefaultVerdi(DEFAULT_VERDI_SKD);
        skdFeltDefinisjon2.setDefaultVerdi(DEFAULT_VERDI_SKD);

        String finalSkdMelding = "test1test1test2test2";

        StringBuilder skdMldSB = skdInputParamsToSkdMeldingInnhold.execute(skdInputMap, skdFelterContainerTrans1);

        assertThat(finalSkdMelding, is(equalTo(skdMldSB.toString())));
    }

    @Test
    public void hvisSkdParametereErNullSaaBrukesDefaultVerdienTilFeltene() {
        skdFeltDefinisjon1.setNokkelNavn("testFelt1");
        skdFeltDefinisjon2.setNokkelNavn("testFelt2");

        skdFeltDefinisjon1.setDefaultVerdi(DEFAULT_VERDI_SKD);
        skdFeltDefinisjon2.setDefaultVerdi(DEFAULT_VERDI_SKD);

        String finalSkdMelding = DEFAULT_VERDI_SKD + DEFAULT_VERDI_SKD;

        StringBuilder skdMldSB = skdInputParamsToSkdMeldingInnhold.execute(skdInputMap, skdFelterContainerTrans1);

        assertThat(finalSkdMelding, is(equalTo(skdMldSB.toString())));
    }

}
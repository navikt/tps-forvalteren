package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SkdFeltDefinisjonerTrans1Test {
    SkdMeldingTrans1 skdMeldingTrans1 = createVigselsmelding();
    String skdMeldingStringUtenHeader = skdMeldingTrans1.toString();

    @Test
    public void extractMeldingsverdiFromString() {
        SkdFeltDefinisjonerTrans1.getAllFeltDefinisjonerInSortedList().forEach(skdFeltDef ->
                assertEquals(skdMeldingTrans1.getMeldingsverdien(skdFeltDef), skdFeltDef.extractMeldingsfeltverdiFromString(skdMeldingStringUtenHeader)));
    }

    private static SkdMeldingTrans1 createVigselsmelding() {
        return SkdMeldingTrans1.builder()
                .vigselstype("1")
                .ektefelleEkteskapPartnerskapNr("1")
                .regdatoSivilstand("20180404")
                .tildelingskode("0")
                .transtype("1")
                .personnummer("52940")
                .regDato("20180404")
                .familienummer("25041650136")
                .maskintid("142656")
                .sivilstand("6")
                .ektefelleTidligereSivilstand("1")
                .vigselskommune("0301")
                .ektefellePartnerPersonnr("50136")
                .ekteskapPartnerskapNr("1")
                .fodselsdato("271116")
                .maskindato("20180404")
                .aarsakskode("61")
                .ektefellePartnerFoedselsdato("250416")
                .tidligereSivilstand("1")
                .regdatoFamnr("20180404").build();

    }

}
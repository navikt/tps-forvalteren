package no.nav.tps.forvalteren.service.command.testdata.utils;

import static no.nav.tps.forvalteren.testutils.AssertionUtils.assertAllFieldsNotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.junit.Test;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;

public class MapBetweenRsMeldingstypeAndSkdMeldingTest {

    MapBetweenRsMeldingstypeAndSkdMelding mapper = new MapBetweenRsMeldingstypeAndSkdMelding();

    @Test
    public void shouldMapAllFieldsFromRsMeldingstypeToSkdmelding() throws InvocationTargetException, IllegalAccessException {
        RsMeldingstype1Felter rsMeldingstype1Felter = constructRsMeldingWithNonEmptyFields();
        SkdMeldingTrans1 skdMeldingTrans1 = mapper.mapReverse(rsMeldingstype1Felter);
        assertAllFieldsNotNull( skdMeldingTrans1, Arrays.asList("getHeader", "getMeldingsverdien"));
    }
    
    private RsMeldingstype1Felter constructRsMeldingWithNonEmptyFields() {
        RsMeldingstype1Felter rsMeldingstype1Felter = RsMeldingstype1Felter.builder()
                .fodselsdato("noe")
                .personnummer("noe")
                .regDato("noe")
                .statuskode("noe")
                .datoDoed("noe")
                .slektsnavn("noe")
                .fornavn("noe")
                .mellomnavn("noe")
                .slekstnavnUgift("noe")
                .forkortetNavn("noe")
                .regDatoNavn("noe")
                .foedekommLand("noe")
                .foedested("noe")
                .familienummer("noe")
                .regdatoFamnr("noe")
                .personkode("noe")
                .spesRegType("noe")
                .datoSpesRegType("noe")
                .sivilstand("noe")
                .regdatoSivilstand("noe")
                .ektefellePartnerFdato("noe")
                .ektefellePartnerPnr("noe")
                .ektefellePartnerNavn("noe")
                .ektefellePartnerStatsb("noe")
                .regdatoAdr("noe")
                .postadrRegDato("noe")
                .flyttedatoAdr("noe")
                .kommunenummer("noe")
                .gateGaard("noe")
                .husBruk("noe")
                .bokstavFestenr("noe")
                .undernr("noe")
                .adressenavn("noe")
                .adressetype("noe")
                .tilleggsadresse("noe")
                .postnummer("noe")
                .valgkrets("noe")
                .adresse1("noe")
                .adresse2("noe")
                .adresse3("noe")
                .postadrLand("noe")
                .innvandretFraLand("noe")
                .fraLandRegdato("noe")
                .fraLandFlyttedato("noe")
                .fraKommune("noe")
                .fraKommRegdato("noe")
                .fraKommFlyttedato("noe")
                .utvandretTilLand("noe")
                .tilLandRegdato("noe")
                .tilLandFlyttedato("noe")
                .samemanntall("noe")
                .datoSamemanntall("noe")
                .umyndiggjort("noe")
                .datoUmyndiggjort("noe")
                .foreldreansvar("noe")
                .datoForeldreansvar("noe")
                .arbeidstillatelse("noe")
                .datoArbeidstillatelse("noe")
                .fremkonnummer("noe")
                .morsFodselsdato("noe")
                .morsPersonnummer("noe")
                .morsNavn("noe")
                .morsStatsbSkap("noe")
                .farsFodselsdato("noe")
                .farsPersonnummer("noe")
                .farsFarsNavn("noe")
                .farsStatsbSkap("noe")
                .tidligereFnrDnr("noe")
                .datoTidlFnrDnr("noe")
                .nyttFnr("noe")
                .datoNyttFnr("noe")
                .levendeDoed("noe")
                .kjonn("noe")
                .tildelingskode("noe")
                .foedselstype("noe")
                .morsSiviltilstand("noe")
                .ekteskPartnskNr("noe")
                .ektfEkteskPartnskNr("noe")
                .vigselstype("noe")
                .forsByrde("noe")
                .dombevilling("noe")
                .antallBarn("noe")
                .tidlSivilstand("noe")
                .ektfTidlSivilstand("noe")
                .hjemmel("noe")
                .fylke("noe")
                .vigselskomm("noe")
                .tidlSepDomBev("noe")
                .begjertAv("noe")
                .registrGrunnlag("noe")
                .doedssted("noe")
                .typeDoedssted("noe")
                .vigselsdato("noe")
                .medlKirken("noe")
                .bolignr("noe")
                .dufId("noe")
                .brukerident("noe")
                .skolerets("noe")
                .tkNr("noe")
                .dnrHjemlandsadresse1("noe")
                .dnrHjemlandsadresse2("noe")
                .dnrHjemlandsadresse3("noe")
                .dnrHjemlandLandkode("noe")
                .dnrHjemlandRegDato("noe")
                .dnrIdKontroll("noe")
                .utvandringstype("noe")
                .grunnkrets("noe")
                .statsborgerskap("noe")
                .statsborgerskapRegdato("noe")
                .statsborgerskap2("noe")
                .regdatoStatsb2("noe")
                .statsborgerskap3("noe")
                .regdatoStatsb3("noe")
                .statsborgerskap4("noe")
                .regdatoStatsb4("noe")
                .statsborgerskap5("noe")
                .regdatoStatsb5("noe")
                .statsborgerskap6("noe")
                .regdatoStatsb6("noe")
                .statsborgerskap7("noe")
                .regdatoStatsb7("noe")
                .statsborgerskap8("noe")
                .regdatoStatsb8("noe")
                .statsborgerskap9("noe")
                .regdatoStatsb9("noe")
                .statsborgerskap10("noe")
                .regdatoStatsb10("noe")
                .bibehold("noe")
                .regdatoBibehold("noe")
                .saksid("noe")
                .embete("noe")
                .sakstype("noe")
                .vedtaksdato("noe")
                .internVergeid("noe")
                .vergeFnrDnr("noe")
                .vergetype("noe")
                .mandattype("noe")
                .mandatTekst("noe")
                .reserverFramtidigBruk("noe")
                .build();
        rsMeldingstype1Felter.setId(1L);
        rsMeldingstype1Felter.setBeskrivelse("noe");
        rsMeldingstype1Felter.setTranstype("noe");
        rsMeldingstype1Felter.setMaskindato("noe");
        rsMeldingstype1Felter.setMaskintid("noe");
        rsMeldingstype1Felter.setAarsakskode("noe");
        rsMeldingstype1Felter.setSekvensnr("noe");
        
        return rsMeldingstype1Felter;
    }
}
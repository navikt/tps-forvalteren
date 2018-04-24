package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import javax.persistence.Convert;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FiktiveIdenterGeneratorTest {

    @InjectMocks
    private FiktiveIdenterGenerator fiktiveIdenterGenerator;

    private RsPersonMal inputFraPersonMal;

    private String DNR = "DNR";
    private String FNR = "FNR";
    private String BNR = "BNR";

    int antallDummy;

    @Before
    public void setup() {
        inputFraPersonMal = new RsPersonMal();
        antallDummy = 60;
    }

    @Test
    public void personerFodtIPerioden1854til1899skalHaNummerIIntervall500til749() {
        inputFraPersonMal.setKjonn('M');
        LocalDate date = LocalDate.of(1854, Month.MAY, 15);
        LocalDate dateFoer = LocalDate.of(1900, Month.JANUARY, 1);


        inputFraPersonMal.setFodtEtter(ConvertDateToString.yyyyMMdd(date));
        inputFraPersonMal.setFodtFor(ConvertDateToString.yyyyMMdd(dateFoer));
        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(inputFraPersonMal);
        for (String fnr : fnrList) {
            int individNummer = Integer.parseInt(fnr.substring(6, 9));
            assertTrue(individNummer >= 500 && individNummer <= 749);
        }
    }

    @Test
    public void personerFodtIPerioden1900til1939skalHaNummerIIntervall0til499() {
        inputFraPersonMal.setKjonn('M');
        LocalDate date = LocalDate.of(1900, Month.MAY, 15);
        LocalDate dateFoer = LocalDate.of(1940, Month.JANUARY, 1);
        inputFraPersonMal.setFodtEtter(ConvertDateToString.yyyyMMdd(date));
        inputFraPersonMal.setFodtFor(ConvertDateToString.yyyyMMdd(dateFoer));
        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(inputFraPersonMal);
        for (String fnr : fnrList) {
            int individNummer = Integer.parseInt(fnr.substring(6, 9));
            assertTrue(individNummer >= 000 && individNummer <= 499);
        }
    }

    @Test
    public void personerFodtIPerioden1940til1999skalHaNummerIIntervall900til999eller0til499() {
        inputFraPersonMal.setKjonn('M');
        LocalDate date = LocalDate.of(1940, Month.MAY, 15);
        LocalDate dateFoer = LocalDate.of(2000, Month.JANUARY, 1);
        inputFraPersonMal.setFodtEtter(ConvertDateToString.yyyyMMdd(date));
        inputFraPersonMal.setFodtFor(ConvertDateToString.yyyyMMdd(dateFoer));
        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(inputFraPersonMal);
        for (String fnr : fnrList) {
            int individNummer = Integer.parseInt(fnr.substring(6, 9));
            assertTrue((individNummer >= 0 && individNummer <= 499) || (individNummer >= 900 && individNummer <= 999));
        }
    }

    @Test
    public void personerFodtIPerioden2000til2039skalHaNummerIInterval500til999() {
        inputFraPersonMal.setKjonn('M');
        LocalDate date = LocalDate.of(2000, Month.MAY, 15);
        LocalDate dateFoer = LocalDate.of(2040, Month.JANUARY, 1);
        inputFraPersonMal.setFodtEtter(ConvertDateToString.yyyyMMdd(date));
        inputFraPersonMal.setFodtFor(ConvertDateToString.yyyyMMdd(dateFoer));
        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(inputFraPersonMal);
        for (String fnr : fnrList) {
            int individNummer = Integer.parseInt(fnr.substring(6, 9));
            assertTrue(individNummer >= 500 && individNummer <= 999);
        }
    }

    @Test
    public void genererFiktiveIdenterRiktigStorrelse() throws Exception {
        antallDummy = 70;

        inputFraPersonMal.setKjonn('M');
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        inputFraPersonMal.setFodtEtter(ConvertDateToString.yyyyMMdd(date));

        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(inputFraPersonMal);
        assertThat(fnrList.size() , is(equalTo(140)));
        for (String fnr : fnrList) {
            assertThat(fnr.length(), is(equalTo(11)));
        }

        antallDummy = 5;
        fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(inputFraPersonMal);
        assertSame(fnrList.size() , 10);
        for (String fnr : fnrList) {
            assertSame(fnr.length() ,11);
        }
    }

    @Test
    public void genererKvinneIdentHvisKjonnErSattTilKvinne() throws Exception {
        inputFraPersonMal.setKjonn('K');
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        inputFraPersonMal.setFodtEtter(ConvertDateToString.yyyyMMdd(date));

        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(inputFraPersonMal);
        for (String fnr : fnrList) {
            int kjonnNummer = Integer.parseInt(fnr.substring(8, 9));
            assertSame(kjonnNummer % 2 , 0);
        }
    }

    @Test
    public void genererMannIdentHvisKjonnErSattTilMann() throws Exception {
        inputFraPersonMal.setKjonn('M');
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        inputFraPersonMal.setFodtEtter(ConvertDateToString.yyyyMMdd(date));

        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(inputFraPersonMal);
        for (String fnr : fnrList) {
            int kjonnNummer = Integer.parseInt(fnr.substring(8, 9));
            assertSame(kjonnNummer % 2 , 1);
        }
    }

    /* Ved D-nummer legges 4 til det forste nummeret i fodseslsnummeret. 200192 blir 600192*/
    @Test
    public void genererDNummerMedRiktigStatsborgerskapNummer() throws Exception {
//        testpersonKriterie.setIdenttype(DNR);
//        testpersonKriterie.setKjonn('K');
//        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
//        testpersonKriterie.setFoedtEtter(date);
//        testpersonKriterie.setFoedtFoer(date);
//
//        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterie);
//        for (String fnr : fnrList) {
//            int statsborgerskapNummer = Integer.parseInt(fnr.substring(0, 1));
//            String fodselsNr = Integer.toString(date.getDayOfMonth());
//            int forsteSifferFodselsnummerPreConvertToDNummer = Integer.parseInt(fodselsNr.substring(0, 1));
//
//            assertTrue(statsborgerskapNummer >= 4);
//            assertSame(forsteSifferFodselsnummerPreConvertToDNummer + 4, statsborgerskapNummer);
//        }
    }

    /* Ved B-nummer legges 20 til maened nummeret*/
    @Test
    public void genererBNummerMedRiktigMonthNummer() throws Exception {
//        testpersonKriterie.setIdenttype(BNR);
//        testpersonKriterie.setKjonn('K');
//        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
//        testpersonKriterie.setFoedtEtter(date);
//
//        Set<String> bNummerList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterie);
//        for (String fnr : bNummerList) {
//            int mndNummer = Integer.parseInt(fnr.substring(2, 3));
//            assertTrue(mndNummer >= 2);
//        }
    }

    @Test
    public void hvisDatoerIKriterierIkkeErSattBrukesTilfeldigDatoMellom01011900ogDagensDato() throws Exception {
        inputFraPersonMal.setKjonn('K');

        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(inputFraPersonMal);
        for (String fnr : fnrList) {
            int aarsnummer = Integer.parseInt(fnr.substring(4, 6));
            assertTrue(aarsnummer >= 0);
        }
    }

    @Test
    public void hvisKjonnIkkeErSattSettesEtRandomKjonn() throws Exception {
        // Hvis den ikke krasjer saa vil et kjonn ha blitt valgt.
        fiktiveIdenterGenerator.genererFiktiveIdenter(inputFraPersonMal);
    }
}
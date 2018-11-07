package no.nav.tps.forvalteren.service.command.testdata;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;

@RunWith(MockitoJUnitRunner.class)
public class FiktiveIdenterGeneratorTest {

    @InjectMocks
    private FiktiveIdenterGenerator fiktiveIdenterGenerator;

    private RsPersonKriterier testpersonKriterier;

    private static final String DNR = "DNR";
    private static final String FNR = "FNR";
    private static final String BNR = "BNR";

    @Before
    public void setup() {
        testpersonKriterier = new RsPersonKriterier();
        testpersonKriterier.setAntall(60);
    }

    @Test
    public void personerFodtIPerioden1854til1899skalHaNummerIIntervall500til749OgDatoInnenforRiktigIntervall() {

        testpersonKriterier.setIdenttype(FNR);
        testpersonKriterier.setKjonn("M");
        LocalDate dateFoer = LocalDate.of(1900, Month.JANUARY, 1);
        LocalDate dateEtter = LocalDate.of(1854, Month.MAY, 15);

        testpersonKriterier.setFoedtFoer(dateFoer);
        testpersonKriterier.setFoedtEtter(dateEtter);
        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterier);
        for (String fnr : fnrList) {
            int individNummer = Integer.parseInt(fnr.substring(6, 9));
            assertTrue(individNummer >= 500 && individNummer <= 749);
            assertThat(fnr.substring(0, 6), is(lessThan(dateEtter.format(DateTimeFormatter.ofPattern("uuMMdd")))));
            assertThat(fnr.substring(0, 6), is(greaterThan(dateFoer.format(DateTimeFormatter.ofPattern("uuMMdd")))));
        }
    }

    @Test
    public void personerFodtIPerioden1900til1939skalHaNummerIIntervall0til499() {
        testpersonKriterier.setIdenttype(FNR);
        testpersonKriterier.setKjonn("M");
        LocalDate dateFoer = LocalDate.of(1940, Month.JANUARY, 1);
        LocalDate dateEtter = LocalDate.of(1900, Month.MAY, 15);
        testpersonKriterier.setFoedtFoer(dateFoer);
        testpersonKriterier.setFoedtEtter(dateEtter);
        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterier);
        for (String fnr : fnrList) {
            int individNummer = Integer.parseInt(fnr.substring(6, 9));
            assertTrue(individNummer >= 000 && individNummer <= 499);
        }
    }

    @Test
    public void personerFodtIPerioden1940til1999skalHaNummerIIntervall900til999eller0til499() {
        testpersonKriterier.setIdenttype(FNR);
        testpersonKriterier.setKjonn("M");
        LocalDate dateFoer = LocalDate.of(2000, Month.JANUARY, 1);
        LocalDate dateEtter = LocalDate.of(1940, Month.MAY, 15);
        testpersonKriterier.setFoedtFoer(dateFoer);
        testpersonKriterier.setFoedtEtter(dateEtter);
        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterier);
        for (String fnr : fnrList) {
            int individNummer = Integer.parseInt(fnr.substring(6, 9));
            assertTrue((individNummer >= 0 && individNummer <= 499) || (individNummer >= 900 && individNummer <= 999));
        }
    }

    @Test
    public void personerFodtIPerioden2000til2039skalHaNummerIInterval500til999() {
        testpersonKriterier.setIdenttype(FNR);
        testpersonKriterier.setKjonn("M");
        LocalDate dateFoer = LocalDate.of(2040, Month.JANUARY, 1);
        LocalDate dateEtter = LocalDate.of(2000, Month.MAY, 15);
        testpersonKriterier.setFoedtFoer(dateFoer);
        testpersonKriterier.setFoedtEtter(dateEtter);
        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterier);
        for (String fnr : fnrList) {
            int individNummer = Integer.parseInt(fnr.substring(6, 9));
            assertTrue(individNummer >= 500 && individNummer <= 999);
        }
    }

    @Test
    public void genererFiktiveIdenterRiktigStorrelse() throws Exception {
        testpersonKriterier.setIdenttype(FNR);
        testpersonKriterier.setKjonn("M");
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        testpersonKriterier.setFoedtEtter(date);

        testpersonKriterier.setKjonn("M");
        LocalDate dateEtter = LocalDate.of(1992, Month.JANUARY, 15);
        testpersonKriterier.setFoedtEtter(dateEtter);

        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterier);
        assertThat(fnrList.size(), is(equalTo(120)));
        for (String fnr : fnrList) {
            assertThat(fnr.length(), is(equalTo(11)));
        }

        testpersonKriterier.setAntall(5);
        fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterier);
        assertThat(fnrList.size(), is(equalTo(10)));
        for (String fnr : fnrList) {
            assertThat(fnr.length(), is(equalTo(11)));
        }
    }

    @Test
    public void genererKvinneIdentHvisKjonnErSattTilKvinne() throws Exception {
        testpersonKriterier.setIdenttype(FNR);
        testpersonKriterier.setKjonn("K");
        LocalDate dateEtter = LocalDate.of(1992, Month.JANUARY, 15);
        testpersonKriterier.setFoedtEtter(dateEtter);

        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterier);
        for (String fnr : fnrList) {
            int kjonnNummer = Integer.parseInt(fnr.substring(8, 9));
            assertThat(kjonnNummer % 2, is(equalTo(0)));
        }
    }

    @Test
    public void genererMannIdentHvisKjonnErSattTilMann() throws Exception {
        testpersonKriterier.setIdenttype(FNR);
        testpersonKriterier.setKjonn("M");
        LocalDate dateEtter = LocalDate.of(1992, Month.JANUARY, 15);
        testpersonKriterier.setFoedtEtter(dateEtter);

        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterier);
        for (String fnr : fnrList) {
            int kjonnNummer = Integer.parseInt(fnr.substring(8, 9));
            assertThat(kjonnNummer % 2, is(equalTo(1)));
        }
    }

    /* Ved D-nummer legges 4 til det forste nummeret i fodseslsnummeret. 200192 blir 600192*/
    @Test
    public void genererDNummerMedRiktigStatsborgerskapNummer() throws Exception {
                testpersonKriterier.setIdenttype(DNR);
                testpersonKriterier.setKjonn("K");
                LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
                testpersonKriterier.setFoedtEtter(date);
                testpersonKriterier.setFoedtFoer(date);

                Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterier);
                for (String fnr : fnrList) {
                    int statsborgerskapNummer = Integer.parseInt(fnr.substring(0, 1));
                    String fodselsNr = Integer.toString(date.getDayOfMonth());
                    int forsteSifferFodselsnummerPreConvertToDNummer = Integer.parseInt(fodselsNr.substring(0, 1));

                    assertTrue(statsborgerskapNummer >= 4);
                    assertSame(forsteSifferFodselsnummerPreConvertToDNummer + 4, statsborgerskapNummer);
                }
    }

    /* Ved B-nummer legges 20 til maened nummeret*/
    @Test
    public void genererBNummerMedRiktigMonthNummer() throws Exception {
        testpersonKriterier.setIdenttype(BNR);
        testpersonKriterier.setKjonn("K");
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        testpersonKriterier.setFoedtEtter(date);

        Set<String> bNummerList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterier);
        for (String fnr : bNummerList) {
            int mndNummer = Integer.parseInt(fnr.substring(2, 3));
            assertTrue(mndNummer >= 2);
        }
    }

    @Test
    public void hvisDatoerIKriterierIkkeErSattBrukesTilfeldigDatoMellom01011900ogDagensDato() throws Exception {
        testpersonKriterier.setIdenttype(FNR);
        testpersonKriterier.setKjonn("K");

        Set<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterier);
        for (String fnr : fnrList) {
            int aarsnummer = Integer.parseInt(fnr.substring(4, 6));
            assertTrue(aarsnummer >= 0);
        }
    }

    @Test
    public void hvisKjonnIkkeErSattSettesEtRandomKjonn() throws Exception {
        // Hvis den ikke krasjer saa vil et kjoenn ha blitt valgt.
        testpersonKriterier.setIdenttype(FNR);
        fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterier);
    }
}
package no.nav.tps.forvalteren.service.command.testdata;


import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FiktiveIdenterGeneratorTest {

    @InjectMocks
    private FiktiveIdenterGenerator fiktiveIdenterGenerator;

    private RsPersonKriterier testpersonKriterie;
    private List<RsPersonKriterier> testpersonKriterieListe;

    private String DNR = "DNR";
    private String FNR = "FNR";
    private String BNR = "BNR";

    @Before
    public void setup() {
        testpersonKriterieListe = new ArrayList<>();
        testpersonKriterie = new RsPersonKriterier();
        testpersonKriterie.setAntall(100);
    }

    @Test
    public void genererIndividnummerIRiktigIntevallEtterAarFodt() throws Exception {
        testpersonKriterie.setIdenttype(FNR);
        testpersonKriterie.setKjonn('M');
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        testpersonKriterie.setFodtEtter(date);
        testpersonKriterieListe.add(testpersonKriterie);

        List<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterieListe);
        for (String fnr : fnrList) {
            int individNummer = Integer.parseInt(fnr.substring(6, 9));
            assertTrue((individNummer >= 0 && individNummer <= 499) || (individNummer >= 900 && individNummer <= 999));
        }

        date = LocalDate.of(1899, Month.MAY, 15);
        testpersonKriterie.setFodtEtter(date);
        fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterieListe);
        for (String fnr : fnrList) {
            int individNummer = Integer.parseInt(fnr.substring(6, 9));
            assertTrue(individNummer >= 500 && individNummer <= 749);
        }

        date = LocalDate.of(2000, Month.MAY, 15);
        testpersonKriterie.setFodtEtter(date);
        fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterieListe);
        for (String fnr : fnrList) {
            int individNummer = Integer.parseInt(fnr.substring(6, 9));
            assertTrue(individNummer >= 500 && individNummer <= 999);
        }
    }

    @Test
    public void genererFiktiveIdenterRiktigStorrelse() throws Exception {
        testpersonKriterie.setIdenttype(FNR);
        testpersonKriterie.setKjonn('M');
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        testpersonKriterie.setFodtEtter(date);
        testpersonKriterieListe.add(testpersonKriterie);

        List<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterieListe);
        assertTrue(fnrList.size() == 100);
        for (String fnr : fnrList) {
            assertTrue(fnr.length() == 11);
        }

        testpersonKriterie.setAntall(5);
        fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterieListe);
        assertTrue(fnrList.size() == 5);
        for (String fnr : fnrList) {
            assertTrue(fnr.length() == 11);
        }
    }

    @Test
    public void genererKvinneIdentHvisKjonnErSattTilKvinne() throws Exception {
        testpersonKriterie.setIdenttype(FNR);
        testpersonKriterie.setKjonn('K');
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        testpersonKriterie.setFodtEtter(date);
        testpersonKriterieListe.add(testpersonKriterie);

        List<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterieListe);
        for (String fnr : fnrList) {
            int kjonnNummer = Integer.parseInt(fnr.substring(8, 9));
            assertTrue(kjonnNummer % 2 == 0);
        }
    }

    @Test
    public void genererMannIdentHvisKjonnErSattTilMann() throws Exception {
        testpersonKriterie.setIdenttype(FNR);
        testpersonKriterie.setKjonn('M');
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        testpersonKriterie.setFodtEtter(date);
        testpersonKriterieListe.add(testpersonKriterie);

        List<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterieListe);
        for (String fnr : fnrList) {
            int kjonnNummer = Integer.parseInt(fnr.substring(8, 9));
            assertTrue(kjonnNummer % 2 == 1);
        }
    }

    @Test
    public void genererDNummerMedRiktigStatsborgerskapNummer() throws Exception {
        testpersonKriterie.setIdenttype(DNR);
        testpersonKriterie.setKjonn('K');
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        testpersonKriterie.setFodtEtter(date);
        testpersonKriterieListe.add(testpersonKriterie);

        List<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterieListe);
        for (String fnr : fnrList) {
            int statsborgerskapNummer = Integer.parseInt(fnr.substring(0, 1));
            assertTrue(statsborgerskapNummer >= 4);
            String fodselsNr = Integer.toString(date.getDayOfMonth());
            int forsteSifferFodselsnummerPreConvertToDNummer = Integer.parseInt(fodselsNr.substring(0, 1));
            assertTrue(forsteSifferFodselsnummerPreConvertToDNummer + 4 == statsborgerskapNummer);
        }
    }

    @Test
    public void genererBNummerMedRiktigMonthNummer() throws Exception {
        testpersonKriterie.setIdenttype(BNR);
        testpersonKriterie.setKjonn('K');
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        testpersonKriterie.setFodtEtter(date);
        testpersonKriterieListe.add(testpersonKriterie);

        List<String> bNummerList = fiktiveIdenterGenerator.genererFiktiveIdenter(testpersonKriterieListe);
        for (String fnr : bNummerList) {
            int mndNummer = Integer.parseInt(fnr.substring(2, 3));
            assertTrue(mndNummer >= 2);
        }
    }
}
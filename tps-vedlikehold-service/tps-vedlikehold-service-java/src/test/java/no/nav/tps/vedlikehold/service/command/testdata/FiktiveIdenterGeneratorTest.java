package no.nav.tps.vedlikehold.service.command.testdata;

import no.nav.tps.vedlikehold.domain.service.command.tps.testdata.Kjonn;
import no.nav.tps.vedlikehold.domain.service.command.tps.testdata.TestDataRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Peter Fløgstad on 18.01.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class FiktiveIdenterGeneratorTest {

    @InjectMocks
    private FiktiveIdenterGenerator fiktiveIdenterGenerator;

    private TestDataRequest testDataRequest;

    @Before
    public void setup(){
        testDataRequest = new TestDataRequest();
        testDataRequest.setAntallIdenter(100);
    }

    @Test
    public void genererIndividnummerIRiktigIntevallEtterAarFodt() throws Exception {
        testDataRequest.setIdentType("Fnr");
        testDataRequest.setKjonn(Kjonn.MANN);
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        testDataRequest.setDato(date);

        List<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testDataRequest);
        for(String fnr : fnrList){
            int individNummer = Integer.parseInt(fnr.substring(6,9));
            assertTrue((individNummer >= 0 && individNummer <= 499) || (individNummer >= 900 && individNummer<= 999));
        }

        date = LocalDate.of(1899, Month.MAY, 15);
        testDataRequest.setDato(date);
        fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testDataRequest);
        for(String fnr : fnrList){
            int individNummer = Integer.parseInt(fnr.substring(6,9));
            assertTrue(individNummer >= 500 && individNummer <= 749);
        }

        date = LocalDate.of(2000, Month.MAY, 15);
        testDataRequest.setDato(date);
        fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testDataRequest);
        for(String fnr : fnrList){
            int individNummer = Integer.parseInt(fnr.substring(6,9));
            assertTrue(individNummer >= 500 && individNummer <= 999);
        }
    }

    @Test
    public void genererFiktiveIdenterRiktigStorrelse() throws Exception {
        testDataRequest.setIdentType("Fnr");
        testDataRequest.setKjonn(Kjonn.MANN);
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        testDataRequest.setDato(date);

        List<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testDataRequest);
        assertTrue(fnrList.size() == 100);
        for(String fnr : fnrList){
            assertTrue(fnr.length() == 11);
        }

        testDataRequest.setAntallIdenter(5);
        fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testDataRequest);
        assertTrue(fnrList.size() == 5);
        for(String fnr : fnrList){
            assertTrue(fnr.length() == 11);
        }
    }

    @Test
    public void genererRiktigKjonn() throws Exception {
        testDataRequest.setIdentType("Fnr");
        testDataRequest.setKjonn(Kjonn.KVINNE);
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        testDataRequest.setDato(date);

        List<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testDataRequest);
        for(String fnr : fnrList) {
            int kjonnNummer = Integer.parseInt(fnr.substring(8,9));
            assertTrue(kjonnNummer%2 == 0);
        }

        testDataRequest.setKjonn(Kjonn.MANN);
        fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testDataRequest);
        for(String fnr : fnrList) {
            int kjonnNummer = Integer.parseInt(fnr.substring(8,9));
            assertTrue(kjonnNummer%2 == 1);
        }
    }

    @Test
    public void genererDNummerMedRiktigStatsborgerskapNummer() throws Exception{
        testDataRequest.setIdentType("Dnr");
        testDataRequest.setKjonn(Kjonn.KVINNE);
        LocalDate date = LocalDate.of(1992, Month.JANUARY, 15);
        testDataRequest.setDato(date);

        List<String> fnrList = fiktiveIdenterGenerator.genererFiktiveIdenter(testDataRequest);
        for(String fnr : fnrList) {
            int statsborgerskapNummer = Integer.parseInt(fnr.substring(0,1));
            assertTrue(statsborgerskapNummer >= 4);
            String fodselsNr = Integer.toString(date.getDayOfMonth());
            int forsteSifferFodselsnummerPreConvertToDNummer = Integer.parseInt(fodselsNr.substring(0,1));
            assertTrue(forsteSifferFodselsnummerPreConvertToDNummer + 4 == statsborgerskapNummer);
        }

    }
}
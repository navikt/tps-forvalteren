package no.nav.tps.vedlikehold.service.command.testdata;

import no.nav.tps.vedlikehold.domain.service.command.tps.testdata.TestDataPerson;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class NavnGeneratorForTestPersonerTest {

    @Test
    public void setTilfeldigNavnForTestPerson() throws Exception {
        TestDataPerson testDataPerson = new TestDataPerson();
        testDataPerson.setKjonn("MANN");
        NavnGeneratorForTestPersoner.setTilfeldigNavnForTestPerson(testDataPerson);
        assertTrue(Arrays.asList(NavnGeneratorForTestPersoner.gutteNavn).contains(testDataPerson.getFornavn()));

        TestDataPerson testDataPersonJente = new TestDataPerson();
        testDataPersonJente.setKjonn("KVINNE");
        NavnGeneratorForTestPersoner.setTilfeldigNavnForTestPerson(testDataPersonJente);
        assertTrue(Arrays.asList(NavnGeneratorForTestPersoner.jenteNavn).contains(testDataPersonJente.getFornavn()));
    }
}
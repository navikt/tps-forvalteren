package no.nav.tps.vedlikehold.service.command.testdata;

import no.nav.tps.vedlikehold.domain.service.tps.testdata.Kjonn;
import no.nav.tps.vedlikehold.domain.service.tps.testdata.TestDataRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Created by Peter Fløgstad on 23.01.2017.
 */
@Service
public class TestDataRequestMapper {

    public TestDataRequest mapParametersToTestDataRequest(Map<String, Object> parameters){
        TestDataRequest testDataRequest = new TestDataRequest();

        //TODO Denne endres til å dynamisk ta parameter å sette "IdentType" og "Kjonn" etc.
        testDataRequest.setAntallIdenter(80);   // Alltid 80 fordi Servicerutinen tar max 80 fnr i en request.
        testDataRequest.setIdentType("Fnr");
        testDataRequest.setKjonn(Kjonn.MANN);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(parameters.get("aksjonsDato").toString(), formatter);
        testDataRequest.setDato(date);
        return testDataRequest;
    }
}

package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils;

import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.ResponseStatus;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class RsTpsReponseMappingUtilsTest {

    private static final String FNR_1 = "01018012345";
    private static final String FNR_2 = "02028012345";
    private static final String NAVN_1 = "en";
    private static final String NAVN_2 = "to";


    private static final String XML_PERSON1 = "<fnr>"+FNR_1+"</fnr><navn>"+NAVN_1+"</navn>";
    private static final String XML_PERSON2 = "<fnr>"+FNR_2+"</fnr><navn>"+NAVN_2+"</navn>";

    private static final String XML_DATA = "<enPerson>"+XML_PERSON1+"</enPerson><enPerson>"+XML_PERSON2+"</enPerson>";
    private static final String XML_RAW = "<message><data>"+XML_DATA+"</data></message>";


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private RsTpsResponseMappingUtils responseMappingUtilsMock;

    @Test
    public void rawXmlIsUnaltered() throws Exception {

        Response response = new Response();
        response.setRawXml(XML_RAW);
        response.setDataXmls(Arrays.asList(XML_PERSON1, XML_PERSON2));
        response.setTotalHits(2);
        response.setStatus(new ResponseStatus());

        TpsServiceRoutineResponse result = responseMappingUtilsMock.convertToTpsServiceRutineResponse(response);

        assertThat(result.getXml(), is(equalTo(response.getRawXml())));
    }

    @Test
    public void mapsXmlData() throws Exception {

        Response response = new Response();
        response.setDataXmls(Collections.singletonList(XML_PERSON1));
        response.setStatus(new ResponseStatus());

        TpsServiceRoutineResponse result = responseMappingUtilsMock.convertToTpsServiceRutineResponse(response);

        assertThat(result.getResponse(), is(notNullValue()));
        assertThat(result.getResponse(), is(instanceOf(LinkedHashMap.class)));

        LinkedHashMap map = (LinkedHashMap)result.getResponse();
        LinkedHashMap data = ((LinkedHashMap)((ArrayList)map.get("data")).get(0));

        assertThat(data.get("navn"), is(NAVN_1));
        assertThat(data.get("fnr"), is(FNR_1));
    }

    @Test
    public void mapsXmlDataWithMultiplePersonResultsInResponse() throws Exception {

        Response response = new Response();
        response.setDataXmls(Collections.singletonList(XML_DATA));
        response.setStatus(new ResponseStatus());

        TpsServiceRoutineResponse result = responseMappingUtilsMock.convertToTpsServiceRutineResponse(response);

        assertThat(result.getResponse(), is(notNullValue()));
        assertThat(result.getResponse(), is(instanceOf(LinkedHashMap.class)));

        LinkedHashMap map = (LinkedHashMap)result.getResponse();
        LinkedHashMap data = ((LinkedHashMap)((ArrayList)map.get("data")).get(0));
        LinkedHashMap data2 = ((LinkedHashMap)((ArrayList)map.get("data")).get(1));

        assertTrue(((ArrayList) map.get("data")).size() == 2);
        assertThat(data.get("navn"), is(NAVN_1));
        assertThat(data.get("fnr"), is(FNR_1));
        assertThat(data2.get("navn"), is(NAVN_2));
        assertThat(data2.get("fnr"), is(FNR_2));
    }


    @Test
    public void mapsStatus() throws Exception {
        Response response = new Response();
        response.setDataXmls(null);
        response.setTotalHits(null);

        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setKode("00");
        responseStatus.setMelding("melding");
        responseStatus.setUtfyllendeMelding("utfyllende melding");

        response.setStatus(responseStatus);

        TpsServiceRoutineResponse result = responseMappingUtilsMock.convertToTpsServiceRutineResponse(response);

        LinkedHashMap map = (LinkedHashMap)result.getResponse();
        LinkedHashMap status = (LinkedHashMap)map.get("status");

        assertThat(status.get("kode"), is("00"));
        assertThat(status.get("melding"), is("melding"));
        assertThat(status.get("utfyllendeMelding"), is("utfyllende melding"));

    }

    @Test
    public void mapsTotalHits() throws Exception {

        Response response = new Response();
        response.setDataXmls(null);
        response.setTotalHits(2);

        response.setStatus(new ResponseStatus());

        TpsServiceRoutineResponse result = responseMappingUtilsMock.convertToTpsServiceRutineResponse(response);

        LinkedHashMap map = (LinkedHashMap)result.getResponse();

        assertThat(map.get("antallTotalt"), is(2));
    }

}

package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.ResponseStatus;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


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

    private static final String XML_D = "<fnr>07018833152</fnr><kortnavn>FLØGSTAD PETER SERGIO</kortnavn><fornavn>PETER SERGIO</fornavn><mellomnavn> </mellomnavn><etternavn>FLØGSTAD</etternavn><spesregType> </spesregType><boAdresse1>EIRIKS GATE 15</boAdresse1><boAdresse2> </boAdresse2><postnr>0650</postnr><boPoststed>OSLO</boPoststed><bolignr>H0202</bolignr><postAdresse1> </postAdresse1><postAdresse2> </postAdresse2><postAdresse3> </postAdresse3><kommunenr>0301</kommunenr><tknr>0316</tknr><tidligereKommunenr>0136</tidligereKommunenr><datoFlyttet>2007-10-15</datoFlyttet><personStatus>Bosatt                          Fnr</personStatus><statsborger>NORGE</statsborger><datoStatsborger>1990-10-31</datoStatsborger><sivilstand>Ugift</sivilstand><datoSivilstand>1988-06-10</datoSivilstand><datoDo> </datoDo><datoUmyndiggjort> </datoUmyndiggjort><innvandretFra>COLOMBIA</innvandretFra><datoInnvandret>1988-06-10</datoInnvandret><utvandretTil> </utvandretTil><datoUtvandret> </datoUtvandret><giroInfo><giroNummer>97134930288</giroNummer><giroTidspunktReg>2010-04-28</giroTidspunktReg><giroSystem>SKD</giroSystem><giroSaksbehandler>AJOURHD</giroSaksbehandler></giroInfo><tlfPrivat><tlfNummer> </tlfNummer><tlfTidspunktReg> </tlfTidspunktReg><tlfSystem> </tlfSystem><tlfSaksbehandler> </tlfSaksbehandler></tlfPrivat><tlfJobb><tlfNummer> </tlfNummer><tlfTidspunktReg> </tlfTidspunktReg><tlfSystem> </tlfSystem><tlfSaksbehandler> </tlfSaksbehandler></tlfJobb><tlfMobil><tlfNummer> </tlfNummer><tlfTidspunktReg> </tlfTidspunktReg><tlfSystem> </tlfSystem><tlfSaksbehandler> </tlfSaksbehandler></tlfMobil><epost><epostAdresse> </epostAdresse><epostTidspunktReg> </epostTidspunktReg><epostSystem> </epostSystem><epostSaksbehandler> </epostSaksbehandler></epost>";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ObjectMapper objectMapperMock;

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
        response.setDataXmls(Arrays.asList(XML_PERSON1, XML_PERSON2));
        response.setStatus(new ResponseStatus());

        TpsServiceRoutineResponse result = responseMappingUtilsMock.convertToTpsServiceRutineResponse(response);

        assertThat(result.getResponse(), is(notNullValue()));
        assertThat(result.getResponse(), is(instanceOf(LinkedHashMap.class)));

        LinkedHashMap map = (LinkedHashMap)result.getResponse();
        LinkedHashMap data = ((LinkedHashMap)((ArrayList)map.get("data")).get(0));
        LinkedHashMap data2 = ((LinkedHashMap)((ArrayList)map.get("data")).get(1));

        assertThat(((ArrayList) map.get("data")).size(), is(2));
        assertThat(data.get("navn"), is(NAVN_1));
        assertThat(data.get("fnr"), is(FNR_1));
        assertThat(data2.get("navn"), is(NAVN_2));
        assertThat(data2.get("fnr"), is(FNR_2));
    }


    @Test
    public void mapsStatus() throws Exception {
        Response response = new Response();

        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setKode("00");
        responseStatus.setMelding("melding");
        responseStatus.setUtfyllendeMelding("utfyllende melding");

        response.setStatus(responseStatus);
        String statusAsString = "00, melding, utfyllende melding";

        LinkedHashMap statusMap = new LinkedHashMap();
        statusMap.put("kode", "00");
        statusMap.put("melding", "melding");
        statusMap.put("utfyllendeMelding", "utfyllende melding");

        when(objectMapperMock.writeValueAsString(responseStatus)).thenReturn(statusAsString);
        when(objectMapperMock.readValue(statusAsString, Map.class)).thenReturn(statusMap);

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

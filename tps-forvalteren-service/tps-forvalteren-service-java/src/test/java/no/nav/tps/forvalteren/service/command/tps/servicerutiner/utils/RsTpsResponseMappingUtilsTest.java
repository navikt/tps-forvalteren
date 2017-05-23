package no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RsTpsResponseMappingUtilsTest {

    private static final String FNR_1 = "01018012345";
    private static final String FNR_2 = "02028012345";
    private static final String NAVN_1 = "en";
    private static final String NAVN_2 = "to";


    private static final String XML_PERSON1 = "<fnr>"+FNR_1+"</fnr><navn>"+NAVN_1+"</navn>";
    private static final String XML_PERSON2 = "<fnr>"+FNR_2+"</fnr><navn>"+NAVN_2+"</navn>";

    private static final String XML_DATA = "<enPerson>"+XML_PERSON1+"</enPerson><enPerson>"+XML_PERSON2+"</enPerson>";
    private static final String XML_RAW = "<message><data>"+XML_DATA+"</data></message>";

    private static final String XML_ARRAY = "<tpsPersonData><tpsServiceRutine><serviceRutinenavn>FS03-FDNUMMER-ADRHISTO-O</serviceRutinenavn><aksjonsDato/><aksjonsKode>B</aksjonsKode><aksjonsKode2>0</aksjonsKode2><fnr>27057047173</fnr></tpsServiceRutine> <tpsSvar><svarStatus><returStatus>00</returStatus><returMelding> </returMelding><utfyllendeMelding> </utfyllendeMelding></svarStatus><personDataS010><fnr>27057047173</fnr><spesregType> </spesregType><kortnavn>STØKKEN HANS-ERLEND</kortnavn><datoDo> </datoDo><antallFS010>8</antallFS010><boAdresser><boAdresse><adresseFom>2010-12-02</adresseFom><adresseTom>2014-12-29</adresseTom><periodeSpesreg> </periodeSpesreg><boAdresse1>GAMLE MOSSEVEI 138</boAdresse1><boAdresse2> </boAdresse2><bolignr> </bolignr><postnr>1433</postnr><poststed>ÅS</poststed><kommunenr>0214</kommunenr><kommuneNavn>Ås</kommuneNavn><tknr>0214</tknr><tkNavn>Ås</tkNavn><endringsType>E</endringsType><beskrEndrType>Endring</beskrEndrType></boAdresse><boAdresse><adresseFom>1998-08-05</adresseFom><adresseTom>2000-03-31</adresseTom><periodeSpesreg> </periodeSpesreg><boAdresse1>GAMLE MOSSEVEI 147</boAdresse1><boAdresse2> </boAdresse2><bolignr> </bolignr><postnr>1433</postnr><poststed>ÅS</poststed><kommunenr>0214</kommunenr><kommuneNavn>Ås</kommuneNavn><tknr>0214</tknr><tkNavn>Ås</tkNavn><endringsType>E</endringsType><beskrEndrType>Endring</beskrEndrType></boAdresse><boAdresse><adresseFom>1997-03-01</adresseFom><adresseTom>1998-08-04</adresseTom><periodeSpesreg> </periodeSpesreg><boAdresse1>JORD GÅRD</boAdresse1><boAdresse2> </boAdresse2><bolignr> </bolignr><postnr>1430</postnr><poststed>ÅS</poststed><kommunenr>0211</kommunenr><kommuneNavn>Vestby</kommuneNavn><tknr>0211</tknr><tkNavn>Vestby</tkNavn><endringsType>E</endringsType><beskrEndrType>Endring</beskrEndrType></boAdresse><boAdresse><adresseFom>1996-03-27</adresseFom><adresseTom>1997-02-28</adresseTom><periodeSpesreg> </periodeSpesreg><boAdresse1>GAMLE MOSSEVEI 140</boAdresse1><boAdresse2> </boAdresse2><bolignr> </bolignr><postnr>1433</postnr><poststed>ÅS</poststed><kommunenr>0214</kommunenr><kommuneNavn>Ås</kommuneNavn><tknr>0214</tknr><tkNavn>Ås</tkNavn><endringsType>E</endringsType><beskrEndrType>Endring</beskrEndrType></boAdresse><boAdresse><adresseFom>1995-03-10</adresseFom><adresseTom>1996-02-06</adresseTom><periodeSpesreg> </periodeSpesreg><boAdresse1>GAMLE MOSSEVEI 138</boAdresse1><boAdresse2> </boAdresse2><bolignr> </bolignr><postnr>1433</postnr><poststed>ÅS</poststed><kommunenr>0214</kommunenr><kommuneNavn>Ås</kommuneNavn><tknr>0214</tknr><tkNavn>Ås</tkNavn><endringsType>E</endringsType><beskrEndrType>Endring</beskrEndrType></boAdresse><boAdresse><adresseFom>1994-06-15</adresseFom><adresseTom>1995-03-09</adresseTom><periodeSpesreg> </periodeSpesreg><boAdresse1>TELAVÅGGATA 2</boAdresse1><boAdresse2>SEKSJ.69</boAdresse2><bolignr> </bolignr><postnr>0564</postnr><poststed>OSLO</poststed><kommunenr>0301</kommunenr><kommuneNavn>Oslo</kommuneNavn><tknr>0315</tknr><tkNavn>GRÜNERLØKKA</tkNavn><endringsType>E</endringsType><beskrEndrType>Endring</beskrEndrType></boAdresse><boAdresse><adresseFom>1993-12-23</adresseFom><adresseTom>1994-06-14</adresseTom><periodeSpesreg> </periodeSpesreg><boAdresse1>UELANDS GATE 61 F</boAdresse1><boAdresse2> </boAdresse2><bolignr> </bolignr><postnr>0460</postnr><poststed>OSLO</poststed><kommunenr>0301</kommunenr><kommuneNavn>Oslo</kommuneNavn><tknr>0314</tknr><tkNavn>SAGENE</tkNavn><endringsType>E</endringsType><beskrEndrType>Endring</beskrEndrType></boAdresse><boAdresse><adresseFom>1993-06-15</adresseFom><adresseTom>1993-12-22</adresseTom><periodeSpesreg> </periodeSpesreg><boAdresse1>SCHØNINGS GATE 41</boAdresse1><boAdresse2> </boAdresse2><bolignr> </bolignr><postnr>0362</postnr><poststed>OSLO</poststed><kommunenr>0301</kommunenr><kommuneNavn>Oslo</kommuneNavn><tknr>0312</tknr><tkNavn>FROGNER</tkNavn><endringsType>E</endringsType><beskrEndrType>Endring</beskrEndrType></boAdresse></boAdresser></personDataS010></tpsSvar></tpsPersonData>";

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

        LinkedHashMap map = (LinkedHashMap)result.getResponse();
        LinkedHashMap data = ((LinkedHashMap)(map.get("data1")));
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
        LinkedHashMap data = ((LinkedHashMap)(map.get("data1")));
        LinkedHashMap data2 = ((LinkedHashMap)(map.get("data2")));

        assertThat(map.get("data2"), is(notNullValue()));
        assertNull(map.get("data3"));

        assertThat(data.get("navn"), is(NAVN_1));
        assertThat(data.get("fnr"), is(FNR_1));
        assertThat(data2.get("navn"), is(NAVN_2));
        assertThat(data2.get("fnr"), is(FNR_2));
    }

    @Test
    public void mapsXMLContainingArrayToArrayInMap() throws Exception {
        Response response = new Response();

        /* SIMPLE */
        String xmlArraySmall = "<fodsel><fnr>1234</fnr><fnr>4321</fnr><fnr>2314</fnr></fodsel>";
        response.setDataXmls(Arrays.asList(xmlArraySmall));

        TpsServiceRoutineResponse resultSimple = responseMappingUtilsMock.convertToTpsServiceRutineResponse(response);

        LinkedHashMap mapSimple = (LinkedHashMap) resultSimple.getResponse();
        LinkedHashMap dataSimple = ((LinkedHashMap)(mapSimple.get("data1")));

        LinkedHashMap fodsel = (LinkedHashMap) dataSimple.get("fodsel");
        ArrayList fnr = (ArrayList) fodsel.get("fnr");

        assertTrue(fnr.size() == 3);
        assertEquals(fnr.get(2), 2314);


        /* FULL TPS RESPONSE */
        response.setDataXmls(Arrays.asList(XML_ARRAY));

        TpsServiceRoutineResponse result = responseMappingUtilsMock.convertToTpsServiceRutineResponse(response);

        LinkedHashMap map = (LinkedHashMap)result.getResponse();
        LinkedHashMap data = ((LinkedHashMap)(map.get("data1")));

        LinkedHashMap personData = (LinkedHashMap) data.get("tpsPersonData");
        LinkedHashMap svar =(LinkedHashMap) personData.get("tpsSvar");
        LinkedHashMap persData =(LinkedHashMap) svar.get("personDataS010");
        LinkedHashMap boAdresser =(LinkedHashMap) persData.get("boAdresser");
        ArrayList boAdresseList =(ArrayList) boAdresser.get("boAdresse");

        assertTrue(boAdresseList.size() == 8);

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
        ResponseStatus status = (ResponseStatus) map.get("status");

        assertThat(status.getKode(), is("00"));
        assertThat(status.getMelding(), is("melding"));
        assertThat(status.getUtfyllendeMelding(), is("utfyllende melding"));

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
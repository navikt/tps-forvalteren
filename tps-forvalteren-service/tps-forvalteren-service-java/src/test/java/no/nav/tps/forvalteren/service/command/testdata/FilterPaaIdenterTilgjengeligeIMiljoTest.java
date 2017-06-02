package no.nav.tps.forvalteren.service.command.testdata;

import com.fasterxml.jackson.databind.JsonNode;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilterPaaIdenterTilgjengeligeIMiljoTest {

    private TpsServiceRoutineRequest tpsServiceRoutineRequestTom;
    private TpsServiceRoutineResponse tpsResponse2Identer, tpsResponse3Identer;
    private LinkedHashMap data1, data2, data3,data4;
    private JsonNode jsonNodeTom;

    private String FNR_1 = "09109009870";
    private String FNR_2 = "09109008815";
    private String FNR_3 = "09109000024";
    private String FNR_4 = "09109000458";

    @Mock
    private UserContextHolder userContextHolderMock;

    @Mock
    private TpsRequestSender tpsRequestSenderMock;

    @Mock
    private RsTpsRequestMappingUtils mappingUtilsMock;

    @InjectMocks
    private FilterPaaIdenterTilgjengeligeIMiljo filterPaaIdenterTilgjengeligeIMiljo;

    @Before
    public void setup() {
        User user = new User("test", "tester");
        tpsServiceRoutineRequestTom = new TpsServiceRoutineRequest();
        tpsResponse2Identer = new TpsServiceRoutineResponse();
        tpsResponse3Identer = new TpsServiceRoutineResponse();

        tpsResponse2Identer.setXml("test");
        tpsResponse3Identer.setXml("test");

        when(userContextHolderMock.getUser()).thenReturn(user);
        when(mappingUtilsMock.convertToTpsServiceRoutineRequest(anyString(), any(Map.class)))
                .thenReturn(tpsServiceRoutineRequestTom);
        when(mappingUtilsMock.convert(any(Map.class), eq(JsonNode.class))).thenReturn(jsonNodeTom);

        data1 = new LinkedHashMap();
        data2 = new LinkedHashMap();
        data3 = new LinkedHashMap();
        data4 = new LinkedHashMap();
        data1.put("fnr", FNR_1);
        data2.put("fnr", FNR_2);
        data3.put("fnr", FNR_3);
        data4.put("fnr", FNR_4);

    }

    @Test
    public void returnererKunIdenterSomErDelAvAlleTpsResponsene() throws Exception {
        LinkedHashMap responseMapT1 = new LinkedHashMap();

        responseMapT1.put("data1", data1);
        responseMapT1.put("data2", data2);
        responseMapT1.put("antallTotalt", 2);

        tpsResponse2Identer.setResponse(responseMapT1);

        LinkedHashMap responseMapT2 = new LinkedHashMap();
        responseMapT2.put("data1", data1);
        responseMapT2.put("data2", data3);
        responseMapT2.put("data3", data4);
        responseMapT2.put("antallTotalt", 3);

        tpsResponse3Identer.setResponse(responseMapT2);

        when(tpsRequestSenderMock.sendTpsRequest(any(TpsServiceRoutineRequest.class), any(TpsRequestContext.class)))
                .thenReturn(tpsResponse2Identer, tpsResponse3Identer);

        List<String> filtrerteIdenter = filterPaaIdenterTilgjengeligeIMiljo.filtrer(Arrays.asList("test"));

        assertThat(filtrerteIdenter, hasItem(FNR_1));
        assertFalse(filtrerteIdenter.contains(FNR_2));
        assertFalse(filtrerteIdenter.contains(FNR_3));
        assertFalse(filtrerteIdenter.contains(FNR_4));
    }

    @Test
    public void returnererIngenIdenterHvisIngenIdenterErAaFinneIAlleMiljoerKunINoenBestemteMiljoer() throws Exception {
        LinkedHashMap responseMapT1 = new LinkedHashMap();
        responseMapT1.put("data1", data1);
        responseMapT1.put("data2", data2);
        responseMapT1.put("antallTotalt", 2);

        tpsResponse2Identer.setResponse(responseMapT1);

        LinkedHashMap responseMapT2 = new LinkedHashMap();
        responseMapT2.put("data1", data3);
        responseMapT2.put("data2", data4);
        responseMapT2.put("antallTotalt", 2);

        tpsResponse3Identer.setResponse(responseMapT2);

        when(tpsRequestSenderMock.sendTpsRequest(any(TpsServiceRoutineRequest.class), any(TpsRequestContext.class)))
                .thenReturn(tpsResponse2Identer, tpsResponse3Identer);

        List<String> filtrerteIdenter = filterPaaIdenterTilgjengeligeIMiljo.filtrer(Arrays.asList("test"));

        assertTrue(filtrerteIdenter.isEmpty());
    }

    @Test
    public void returnererAlleIdenterEtterspurtHvisAlleErTilgjengeligIAlleMiljoer() throws Exception {

        LinkedHashMap responseMapT1 = new LinkedHashMap();
        responseMapT1.put("data1", data1);
        responseMapT1.put("data2", data2);
        responseMapT1.put("antallTotalt", 2);

        tpsResponse2Identer.setResponse(responseMapT1);

        LinkedHashMap responseMapT2 = new LinkedHashMap();
        responseMapT2.put("data1", data1);
        responseMapT2.put("data2", data2);
        responseMapT2.put("antallTotalt", 2);

        tpsResponse3Identer.setResponse(responseMapT2);

        when(tpsRequestSenderMock.sendTpsRequest(any(TpsServiceRoutineRequest.class), any(TpsRequestContext.class)))
                .thenReturn(tpsResponse2Identer, tpsResponse3Identer);

        List<String> filtrerteIdenter = filterPaaIdenterTilgjengeligeIMiljo.filtrer(Arrays.asList("test"));

        assertThat(filtrerteIdenter, hasItems(FNR_1,FNR_2));
    }

    @Test
    public void returnererTomListeHvisIdenteneManOnskerIkkeErTilgjengeligINoenMiljoer() throws Exception {
        LinkedHashMap responseMapT1 = new LinkedHashMap();
        responseMapT1.put("antallTotalt", 0);

        tpsResponse2Identer.setResponse(responseMapT1);

        LinkedHashMap responseMapT2 = new LinkedHashMap();
        responseMapT2.put("antallTotalt", 0);

        tpsResponse3Identer.setResponse(responseMapT2);

        when(tpsRequestSenderMock.sendTpsRequest(any(TpsServiceRoutineRequest.class), any(TpsRequestContext.class)))
                .thenReturn(tpsResponse2Identer, tpsResponse3Identer);

        List<String> filtrerteIdenter = filterPaaIdenterTilgjengeligeIMiljo.filtrer(Arrays.asList("test"));

        assertTrue(filtrerteIdenter.isEmpty());
    }
}
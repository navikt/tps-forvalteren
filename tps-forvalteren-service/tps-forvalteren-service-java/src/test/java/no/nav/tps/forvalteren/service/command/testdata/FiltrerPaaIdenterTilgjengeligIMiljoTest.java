package no.nav.tps.forvalteren.service.command.testdata;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class FiltrerPaaIdenterTilgjengeligIMiljoTest {

    private TpsServiceRoutineRequest tpsServiceRoutineRequestTom;
    private TpsServiceRoutineResponse tpsResponse2Identer, tpsResponse3Identer;
    private Map data1, data2, data3, data4;
    private ResponseStatus responseStatusDummy;

    private int ANTALL_LOOP_I_EN_KJOERING = 1;

    private String FNR_1 = "09109009870";
    private String FNR_2 = "09109008815";
    private String FNR_3 = "09109000024";
    private String FNR_4 = "09109000458";

    private Set<String> env = new HashSet<>();

    @Mock
    private UserContextHolder userContextHolder;

    @Mock
    private TpsRequestSender tpsRequestSender;

    @Mock
    private RsTpsRequestMappingUtils tpsRequestMappingUtils;

    @InjectMocks
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;

    @Before
    public void setup() {
        env.add("q");

        User user = new User("test", "tester");
        tpsServiceRoutineRequestTom = new TpsServiceRoutineRequest();
        tpsResponse2Identer = new TpsServiceRoutineResponse();
        tpsResponse3Identer = new TpsServiceRoutineResponse();

        tpsResponse2Identer.setXml("test");
        tpsResponse3Identer.setXml("test");

        when(userContextHolder.getUser()).thenReturn(user);

        when(tpsRequestMappingUtils.convertToTpsServiceRoutineRequest(anyString(), any(Map.class)))
                .thenReturn(tpsServiceRoutineRequestTom);

        data1 = new HashMap();
        data2 = new HashMap();
        data3 = new HashMap();
        data4 = new HashMap();
        data1.put("fnr", FNR_1);
        data2.put("fnr", FNR_2);
        data3.put("fnr", FNR_3);
        data4.put("fnr", FNR_4);

        responseStatusDummy = new ResponseStatus();
        responseStatusDummy.setKode("test");
    }

    @Test
    public void hvisIdenterForSjekkErStorreEnnMaxAntallForRequestSaaKjoresRequestFlereGanger() {
        tpsServiceRoutineRequestTom = new TpsServiceRoutineRequest();

        tpsResponse2Identer.setXml("ikke tom");
        tpsResponse2Identer.setResponse(createTpsResponseMap2Identer());

        when(tpsRequestSender.sendTpsRequest(any(TpsServiceRoutineRequest.class), any(TpsRequestContext.class)))
                .thenReturn(tpsResponse2Identer);

        filtrerPaaIdenterTilgjengeligIMiljo.filtrer(lagIdenterListe(100), env);
        verify(tpsRequestSender, times(ANTALL_LOOP_I_EN_KJOERING * 2)).sendTpsRequest(any(), any());
    }

    @Test
    public void edgeCasesTestHvisIdenterForSjekkErStorreEnnMaxAntallForRequestSaaKjoresRequestFlereGanger() {
        tpsServiceRoutineRequestTom = new TpsServiceRoutineRequest();

        tpsResponse2Identer.setXml("ikke tom");
        tpsResponse2Identer.setResponse(createTpsResponseMap2Identer());

        when(tpsRequestSender.sendTpsRequest(any(TpsServiceRoutineRequest.class), any(TpsRequestContext.class)))
                .thenReturn(tpsResponse2Identer);

        filtrerPaaIdenterTilgjengeligIMiljo.filtrer(lagIdenterListe(80), env);
        verify(tpsRequestSender, times(ANTALL_LOOP_I_EN_KJOERING)).sendTpsRequest(any(), any());

        filtrerPaaIdenterTilgjengeligIMiljo.filtrer(lagIdenterListe(81), env);
        verify(tpsRequestSender, times(ANTALL_LOOP_I_EN_KJOERING + ANTALL_LOOP_I_EN_KJOERING * 2)).sendTpsRequest(any(), any());

        filtrerPaaIdenterTilgjengeligIMiljo.filtrer(lagIdenterListe(160), env);
        verify(tpsRequestSender, times(ANTALL_LOOP_I_EN_KJOERING * 3 + ANTALL_LOOP_I_EN_KJOERING * 2)).sendTpsRequest(any(), any());

        filtrerPaaIdenterTilgjengeligIMiljo.filtrer(lagIdenterListe(161), env);
        verify(tpsRequestSender, times(ANTALL_LOOP_I_EN_KJOERING * 5 + ANTALL_LOOP_I_EN_KJOERING * 3)).sendTpsRequest(any(), any());
    }

    private Map createTpsResponseMap2Identer() {
        Map responseMapT1 = new HashMap();
        responseMapT1.put("status", responseStatusDummy);
        responseMapT1.put("antallTotalt", 2);
        responseMapT1.put("data1", data1);
        responseMapT1.put("data2", data2);
        return responseMapT1;
    }

    @Test
    public void returnererKunIdenterSomErDelAvAlleTpsResponsene() {
        env.add("q2");

        Map svarStatus = new HashMap();
        svarStatus.put("returStatus", "08");
        data1.put("svarStatus", svarStatus);

        Map responseMapT1 = new HashMap();

        responseMapT1.put("data1", data1);
        responseMapT1.put("data2", data2);
        responseMapT1.put("status", responseStatusDummy);
        responseMapT1.put("antallTotalt", 2);

        tpsResponse2Identer.setResponse(responseMapT1);

        Map responseMapT2 = new HashMap();
        responseMapT2.put("data1", data1);
        responseMapT2.put("data2", data3);
        responseMapT2.put("data3", data4);
        responseMapT2.put("status", responseStatusDummy);
        responseMapT2.put("antallTotalt", 3);

        tpsResponse3Identer.setResponse(responseMapT2);

        when(tpsRequestSender.sendTpsRequest(any(TpsServiceRoutineRequest.class), any(TpsRequestContext.class)))
                .thenReturn(tpsResponse2Identer, tpsResponse3Identer);

        Set<String> filtrerteIdenter = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(Arrays.asList(FNR_1, FNR_2, FNR_3, FNR_4), env);

        assertThat(filtrerteIdenter, hasItem(FNR_1));

        assertFalse(filtrerteIdenter.contains(FNR_2));
        assertFalse(filtrerteIdenter.contains(FNR_3));
        assertFalse(filtrerteIdenter.contains(FNR_4));
    }

    @Test
    public void returnererIngenIdenterHvisIngenIdenterErAaFinneIAlleMiljoerKunINoenBestemteMiljoer() throws Exception {
        env.add("q2");

        Map responseMapT1 = new HashMap();
        responseMapT1.put("data1", data1);
        responseMapT1.put("data2", data2);
        responseMapT1.put("status", responseStatusDummy);
        responseMapT1.put("antallTotalt", 2);

        tpsResponse2Identer.setResponse(responseMapT1);

        Map responseMapT2 = new HashMap();
        responseMapT2.put("data1", data3);
        responseMapT2.put("data2", data4);
        responseMapT2.put("status", responseStatusDummy);
        responseMapT2.put("antallTotalt", 2);

        tpsResponse3Identer.setResponse(responseMapT2);

        when(tpsRequestSender.sendTpsRequest(any(TpsServiceRoutineRequest.class), any(TpsRequestContext.class)))
                .thenReturn(tpsResponse2Identer, tpsResponse3Identer);

        Set<String> filtrerteIdenter = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(Arrays.asList(FNR_1, FNR_2), env);

        assertTrue(filtrerteIdenter.isEmpty());
    }

    @Test
    public void returnererAlleIdenterEtterspurtHvisAlleErTilgjengeligIAlleMiljoer() throws Exception {
        env.add("q2");

        Map svarStatus = new HashMap();
        svarStatus.put("returStatus", "08");
        data1.put("svarStatus", svarStatus);
        data2.put("svarStatus", svarStatus);

        LinkedHashMap responseMapT1 = new LinkedHashMap();
        responseMapT1.put("data1", data1);
        responseMapT1.put("data2", data2);
        responseMapT1.put("status", responseStatusDummy);
        responseMapT1.put("antallTotalt", 2);

        tpsResponse2Identer.setResponse(responseMapT1);

        LinkedHashMap responseMapT2 = new LinkedHashMap();
        responseMapT2.put("data1", data1);
        responseMapT2.put("data2", data2);
        responseMapT2.put("status", responseStatusDummy);
        responseMapT2.put("antallTotalt", 2);

        tpsResponse3Identer.setResponse(responseMapT2);

        when(tpsRequestSender.sendTpsRequest(any(TpsServiceRoutineRequest.class), any(TpsRequestContext.class)))
                .thenReturn(tpsResponse2Identer, tpsResponse3Identer);

        Set<String> filtrerteIdenter = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(Arrays.asList(FNR_1, FNR_2), env);

        assertThat(filtrerteIdenter, hasItems(FNR_1, FNR_2));
    }

    @Test
    public void returnererTomListeHvisIdenteneManOnskerIkkeErTilgjengeligINoenMiljoer() throws Exception {
        env.add("q2");

        Map responseMapT1 = new HashMap();
        responseMapT1.put("data1", data1);
        responseMapT1.put("status", responseStatusDummy);
        responseMapT1.put("antallTotalt", 1);

        tpsResponse2Identer.setResponse(responseMapT1);

        Map responseMapT2 = new HashMap();
        responseMapT2.put("data1", data2);
        responseMapT2.put("status", responseStatusDummy);
        responseMapT2.put("antallTotalt", 1);

        tpsResponse3Identer.setResponse(responseMapT2);

        when(tpsRequestSender.sendTpsRequest(any(TpsServiceRoutineRequest.class), any(TpsRequestContext.class)))
                .thenReturn(tpsResponse2Identer, tpsResponse3Identer);

        Set<String> filtrerteIdenter = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(Arrays.asList(FNR_1, FNR_2), env);

        assertTrue(filtrerteIdenter.isEmpty());
    }

    private List<String> lagIdenterListe(int antallIdenter) {
        List<String> identer = new ArrayList<>();
        for (int i = 0; i < antallIdenter; i++) {
            identer.add(String.valueOf(i));
        }
        return identer;
    }
}
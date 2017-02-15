package no.nav.tps.vedlikehold.service.command.testdata;

import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.vedlikehold.domain.service.tps.testdata.TestDataRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.JsonNode;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Peter Fløgstad on 23.01.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class TPSFodselsnummerFetcherTest {

    private final String[] fodselsnummer = {"07070707071", "07070707072","07070707073","07070707074", "07070707075"};
    private static final String SERVICE_RUTINE_NAME = "serviceRutineName";
    private static final String ENVIRONMENT_U = "t0";
    private final String[] data = {"07070707071", "07070707072","07070707073","07070707074", "07070707075"};

    private ArrayList<String> dataList;
    private LinkedHashMap<String, ArrayList> response = new LinkedHashMap<>();
    private TestDataRequest testDataRequest;
    private HashMap<String, Object> tpsParameters = new HashMap<>();
    private TpsServiceRoutineResponse tpsServiceRoutineResponse;
    private TpsRequestContext tpsRequestContext;
    private TpsServiceRoutineRequest tpsServiceRoutineRequest;

    @Mock
    private RsTpsRequestMappingUtils mappingUtilsMock;

    @Mock
    private FiktiveIdenterGenerator fiktiveIdenterGeneratorMock;

    @Mock
    private TpsRequestSender tpsRequestSenderMock;

    @Mock
    private JsonNode baseJsonNode;

    @InjectMocks
    private TPSFodselsnummerFetcher tpsFodselsnummerFetcher;

    @Before
    public void setUp() throws Exception {
        dataList = new ArrayList<>(Arrays.asList(data));
        response.put("data", dataList);
        tpsServiceRoutineResponse = new TpsServiceRoutineResponse();
        tpsRequestContext = new TpsRequestContext();
        tpsServiceRoutineRequest = new TpsServiceRoutineRequest();
        tpsServiceRoutineResponse.setResponse(response);
        when(fiktiveIdenterGeneratorMock.genererFiktiveIdenter(any(TestDataRequest.class))).thenReturn(Arrays.asList(fodselsnummer));
        when(mappingUtilsMock.convert(any(Map.class), eq(JsonNode.class))).thenReturn(baseJsonNode);
        when(mappingUtilsMock.convertToTpsServiceRoutineRequest(SERVICE_RUTINE_NAME, baseJsonNode)).thenReturn(tpsServiceRoutineRequest);
        when(tpsRequestSenderMock.sendTpsRequest(any(TpsServiceRoutineRequest.class), any(TpsRequestContext.class))).thenReturn(tpsServiceRoutineResponse);
    }

    @Test
    public void stopFetchingIfNeverFindsEnoughLedigeFodselsnummer() throws Exception{
        // Siden lopen kjorer maks 10 ganger, og i testen så finner den 5 hver gang, så vil den loop for mange ganger
        testDataRequest = new TestDataRequest();
        tpsParameters.put("antall", 100);
        tpsFodselsnummerFetcher.hentUbrukteFodselsnummereFraTPS( tpsParameters,tpsRequestContext, testDataRequest);

    }

    //TODO Legg til flere tester når litt mer er avklart om hva som kommer inn frontend. Og klasser er helt ferdigstilt
    //TODO -- Vet jo ikke heeelt hva som kommer som param fra Frontend i Fnr-henting siden. Kan være endringer må gjøres...

}
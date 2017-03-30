package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.tps.TpsRequestService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.FindServiceRoutineByName;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ServiceControllerTest {
    private static final String FNR = "12345678910";
    private static final String SERVICE_RUTINE_NAME = "serviceRutineName";
    private static final String ENVIRONMENT_U = "u1";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private UserContextHolder userContextHolderMock;

    @Mock
    private TpsRequestService tpsRequestServiceMock;

    @Mock
    private FindServiceRoutineByName findServiceRoutineByName;

    @Mock
    private TpsServiceRoutineDefinition tpsServiceRoutineDefinitionMock;

    @Mock
    private TpsServiceRoutineRequest serviceRoutineRequestMock;

    @Mock
    private RsTpsRequestMappingUtils mappingUtilsMock;

    @Mock
    private MessageProvider messageProviderMock;

    @Mock
    private RsTpsResponseMappingUtils rsTpsResponseMappingUtilsMock;

    @Mock
    private JsonNode baseJsonNode;

    @InjectMocks
    private ServiceController controller;

    @Before
    @SuppressWarnings("unchecked")
    public void before() {
        mockNodeContent(baseJsonNode, "environment", ENVIRONMENT_U);
        mockNodeContent(baseJsonNode, "serviceRutinenavn", SERVICE_RUTINE_NAME);

        when(mappingUtilsMock.convert(any(Map.class), eq(JsonNode.class))).thenReturn(baseJsonNode);
        when(mappingUtilsMock.convertToTpsServiceRoutineRequest(SERVICE_RUTINE_NAME, baseJsonNode)).thenReturn(serviceRoutineRequestMock);

        when(findServiceRoutineByName.execute(anyString())).thenReturn(Optional.of(tpsServiceRoutineDefinitionMock));
        when(serviceRoutineRequestMock.getServiceRutinenavn()).thenReturn(SERVICE_RUTINE_NAME);

    }

    @Test
    public void getServiceUsingHttpGetSetsServiceRoutineNameOnParameters() {
        mockNodeContent(baseJsonNode, "fnr", FNR);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("environment", ENVIRONMENT_U);
        parameters.put("fnr", FNR);

        controller.getService(parameters, SERVICE_RUTINE_NAME);

        assertThat(parameters.get("serviceRutinenavn"), is(SERVICE_RUTINE_NAME));
    }

    @Test
    public void getServiceUsingHttpGetCallsMappingUtilsWithMap() {
        mockNodeContent(baseJsonNode, "fnr", FNR);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("environment", ENVIRONMENT_U);
        parameters.put("fnr", FNR);

        controller.getService(parameters, SERVICE_RUTINE_NAME);

        verify(mappingUtilsMock).convert(parameters, JsonNode.class);
    }

    @Test
    public void getServiceCallsMappingUtilsOnResponseFromRequestService() throws Exception {
        mockNodeContent(baseJsonNode, "fnr", FNR);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("environment", ENVIRONMENT_U);
        parameters.put("fnr", FNR);

        Response response = mock(Response.class);

        when(tpsRequestServiceMock.executeServiceRutineRequest(any(), any(), any())).thenReturn(response);

        controller.getService(parameters, SERVICE_RUTINE_NAME);

        verify(rsTpsResponseMappingUtilsMock).convertToTpsServiceRutineResponse(response);
    }

    @Test
    public void getServiceReturnsMappedTpsResponse() throws Exception {
        /*mockNodeContent(baseJsonNode, "fnr", FNR);

        TpsServiceRoutineResponse response = mock(TpsServiceRoutineResponse.class);
        Response r = mock(Response.class);

        when(tpsRequestServiceMock.executeServiceRutineRequest(
                any(TpsServiceRoutineRequest.class),
                eq(tpsServiceRoutineDefinitionMock),
                any(TpsRequestContext.class))).thenReturn(r);

        when(rsTpsResponseMappingUtilsMock.convertToResponse(r)).thenReturn(response);

        TpsServiceRoutineResponse result = controller.getService(baseJsonNode);

        assertThat(result, is(sameInstance(response)));
        */
    }


    private void mockNodeContent(JsonNode node, String key, Object value) {
        when(node.has(key)).thenReturn(value != null);
        when(node.asText()).thenReturn(value != null ? value.toString() : null);
        JsonNode subNode = mock(JsonNode.class);
        when(subNode.asText()).thenReturn(value != null ? value.toString() : null);
        when(node.get(key)).thenReturn(subNode);
    }
}
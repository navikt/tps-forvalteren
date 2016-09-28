package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.RsRequestMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpBadRequestException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpInternalServerErrorException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.TpsServiceRutineService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
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
    private TpsServiceRutineService tpsRutineServiceMock;

    @Mock
    private TpsAuthorisationService authorisationServiceMock;

    @Mock
    private RsRequestMappingUtils mappingUtilsMock;

    @Mock
    private MessageProvider messageProviderMock;

    @Mock
    private JsonNode baseJsonNode;

    @InjectMocks
    private ServiceController controller;

    @Before
    @SuppressWarnings("unchecked")
    public void before() {
        mockNodeContent(baseJsonNode, "fnr", FNR);
        mockNodeContent(baseJsonNode, "environment", ENVIRONMENT_U);
        mockNodeContent(baseJsonNode, "serviceRutinenavn", SERVICE_RUTINE_NAME);

        when(mappingUtilsMock.convert(any(Map.class), eq(JsonNode.class))).thenReturn(baseJsonNode);
        when(authorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), any(String.class), any(String.class))).thenReturn(true);
    }

    @Test
    public void getServiceUsingHttpGetSetsServiceRoutineNameOnParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("environment", ENVIRONMENT_U);
        parameters.put("fnr", FNR);

        controller.getService(parameters, SERVICE_RUTINE_NAME);

        assertThat(parameters.get("serviceRutinenavn"), is(SERVICE_RUTINE_NAME));
    }

    @Test
    public void getServiceUsingHttpGetCallsMappingUtilsWithMap() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("environment", ENVIRONMENT_U);
        parameters.put("fnr", FNR);

        controller.getService(parameters, SERVICE_RUTINE_NAME);

        verify(mappingUtilsMock).convert(parameters, JsonNode.class);
    }

    @Test
    public void getServiceThrowsBadRequestWhenMissingEnvironment() {
        mockNodeContent(baseJsonNode, "environment", null);
        when(messageProviderMock.get("rest.service.request.exception.MissingRequiredParams")).thenReturn("val");

        expectedException.expect(HttpBadRequestException.class);
        expectedException.expectMessage("val");

        controller.getService(baseJsonNode);
    }

    @Test
    public void getServiceThrowsBadRequestWhenMissingServiceRutinenavn() {
        mockNodeContent(baseJsonNode, "serviceRutinenavn", null);
        when(messageProviderMock.get("rest.service.request.exception.MissingRequiredParams")).thenReturn("val");

        expectedException.expect(HttpBadRequestException.class);
        expectedException.expectMessage("val");

        controller.getService(baseJsonNode);
    }

    @Test
    public void getServiceDoesNotCallAuthorizationServiceWhenFnrIsEmpty() {
        mockNodeContent(baseJsonNode, "fnr", "");

        controller.getService(baseJsonNode);

        verify(authorisationServiceMock, never()).userIsAuthorisedToReadPersonInEnvironment(any(User.class), any(String.class), any(String.class));
    }

    @Test
    public void getServiceCallsAuthorizationServiceWhenFnrIsSet() {
        mockNodeContent(baseJsonNode, "fnr", "13");

        controller.getService(baseJsonNode);

        verify(authorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), any(String.class), any(String.class));
    }

    @Test
    public void getServiceCallsAuthorizationServiceUsingUserFnrAndEnvironment() {
        User user = mock(User.class);
        when(userContextHolderMock.getUser()).thenReturn(user);

        controller.getService(baseJsonNode);

        verify(authorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(user, FNR, ENVIRONMENT_U);
    }

    @Test
    public void getServiceThrowsUnauthorizedWhenAuthorizationFails() {
        when(messageProviderMock.get("rest.service.request.exception.Unauthorized")).thenReturn("val");
        when(authorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), any(String.class), any(String.class))).thenReturn(false);

        expectedException.expect(HttpUnauthorisedException.class);
        expectedException.expectMessage("val");

        controller.getService(baseJsonNode);
    }

    @Test
    public void getServiceThrowsInternalServerErrorWhenServiceRoutineFailed() throws Exception {
        when(tpsRutineServiceMock.execute(any(TpsRequest.class))).thenThrow(new IllegalArgumentException());

        expectedException.expect(HttpInternalServerErrorException.class);

        controller.getService(baseJsonNode);
    }

    @Test
    public void getServiceReturnsResultFromServiceRutineService() throws Exception {
        ServiceRoutineResponse response = mock(ServiceRoutineResponse.class);
        when(tpsRutineServiceMock.execute(any(TpsRequest.class))).thenReturn(response);

        ServiceRoutineResponse result = controller.getService(baseJsonNode);

        assertThat(result, is(sameInstance(response)));
    }

    private void mockNodeContent(JsonNode node, String key, Object value) {
        when(node.has(key)).thenReturn(value != null);
        when(node.asText()).thenReturn(value != null ? value.toString() : null);
        JsonNode subNode = mock(JsonNode.class);
        when(subNode.asText()).thenReturn(value != null ? value.toString() : null);
        when(node.get(key)).thenReturn(subNode);
    }

}
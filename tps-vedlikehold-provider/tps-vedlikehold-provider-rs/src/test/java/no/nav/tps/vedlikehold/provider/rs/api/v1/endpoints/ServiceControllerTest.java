package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.servlet.http.HttpSession;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequest;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpBadRequestException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpInternalServerErrorException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.DefaultTpsAuthorisationService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.DefaultTpsServiceRutineService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class ServiceControllerTest {

    private static final List<TpsServiceRoutine> SERVICES = mock(List.class);

    private static final String FNR                 = "12345678910";
    private static final String SERVICE_RUTINE_NAME = "serviceRutineName";
    private static final String REQUEST_MESSAGE     = "requestMessage";

    private static final String ENVIRONMENT_U   = "u1";
    private static final String ENVIRONMENT_T   = "t1";
    private static final String ENVIRONMENT_Q   = "q1";
    private static final String ENVIRONMENT_P   = "p1";

    @Mock
    GetTpsServiceRutinerService getTpsServiceRutinerService;

    @Mock
    UserContextHolder userContextHolderMock;

    @Mock
    DefaultTpsServiceRutineService defaultGetTpsServiceRutineServiceMock;

    @Mock
    DefaultTpsAuthorisationService defaultTpsAuthorisationServiceMock;

    @Spy
    ObjectMapper objectMapperMock;

    @Mock
    JsonNode jsonNode;

    @Mock
    JsonNode fnrJsonNodeMock;

    @Mock
    JsonNode environmentJsonNodeMock;

    @Mock
    JsonNode serviceRutinenavnJsonNodeMock;

    @InjectMocks
    ServiceController serviceController;

    @Before
    public void setUp() {
        when(jsonNode.has(eq("fnr"))).thenReturn(true);
        when(jsonNode.has(eq("environment"))).thenReturn(true);
        when(jsonNode.has(eq("serviceRutinenavn"))).thenReturn(true);

        when(jsonNode.get(eq("fnr"))).thenReturn(fnrJsonNodeMock);
        when(jsonNode.get(eq("environment"))).thenReturn(environmentJsonNodeMock);
        when(jsonNode.get(eq("serviceRutinenavn"))).thenReturn(serviceRutinenavnJsonNodeMock);

        when(fnrJsonNodeMock.asText()).thenReturn(FNR);
        when(environmentJsonNodeMock.asText()).thenReturn(ENVIRONMENT_U);
        when(serviceRutinenavnJsonNodeMock.asText()).thenReturn(SERVICE_RUTINE_NAME);

        when(defaultTpsAuthorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString()))
                .thenReturn(true);

        when(objectMapperMock.convertValue(eq(jsonNode), any(Class.class))).thenReturn(new TpsRequest());

        when(getTpsServiceRutinerService.exectue()).thenReturn(SERVICES);
    }

    @Test(expected = HttpUnauthorisedException.class)
    public void getServiceGETCallsGetServiceWithAllParameters() throws Exception {
        when(defaultTpsAuthorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString()))
                .thenReturn(false);

        Map<String, Object> parameters = spy(new HashMap<>());

        parameters.put("environment", ENVIRONMENT_U);
        parameters.put("fnr", FNR);

        serviceController.getService(mock(HttpSession.class), ENVIRONMENT_U, parameters, SERVICE_RUTINE_NAME);

        verify(parameters).put(eq("serviceRutinenavn"), eq(SERVICE_RUTINE_NAME));

        verify(defaultTpsAuthorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), eq(FNR), eq(SERVICE_RUTINE_NAME));
    }

    @Test
    public void getServiceAuthorisesTheUserBeforeContactingTps() throws Exception {
        serviceController.getService(mock(HttpSession.class), jsonNode);

        InOrder inOrder = inOrder(defaultGetTpsServiceRutineServiceMock, defaultTpsAuthorisationServiceMock);

        inOrder.verify(defaultTpsAuthorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString());
        inOrder.verify(defaultGetTpsServiceRutineServiceMock).execute(any(TpsRequest.class));
    }

    @Test(expected = HttpUnauthorisedException.class)
    public void getServiceDeniesAccessIfUserIsUnauthorised() throws Exception {
        when(defaultTpsAuthorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString()))
                .thenReturn(false);

        serviceController.getService(mock(HttpSession.class), jsonNode);

        verify(defaultTpsAuthorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString());
        verify(defaultGetTpsServiceRutineServiceMock, never()).execute(any(TpsRequest.class));
    }

    @Test
    public void environmentIsSentToTheService() throws Exception {
        serviceController.getService(mock(HttpSession.class), jsonNode);
        verify(defaultTpsAuthorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), eq(ENVIRONMENT_U));
        verify(defaultGetTpsServiceRutineServiceMock).execute(any(TpsRequest.class));
    }

    @Test
    public void getTpsServiceRutinerReturnsServices() {
        Collection<TpsServiceRoutine> services = serviceController.getTpsServiceRutiner();

        assertThat(services, is(equalTo(SERVICES)));
    }

    @Test(expected = HttpBadRequestException.class)
    public void badRequestIsThrownIfEnvironmentAndServiceRutinenavnIsNotProvided() throws Exception {

        JsonNode jsonNode = mock(JsonNode.class);

        when(jsonNode.has(eq("serviceRutinenavn"))).thenReturn(false);
        when(jsonNode.has(eq("environment"))).thenReturn(false);

        serviceController.getService(mock(HttpSession.class), jsonNode);
    }

    @Test(expected = HttpInternalServerErrorException.class)
    public void internalServerErrorIsThrownIfAnExceptionIsEncountered() throws Exception {
        when(defaultGetTpsServiceRutineServiceMock.execute(any(TpsRequest.class)))
                .thenThrow(JMSException.class);

        serviceController.getService(mock(HttpSession.class), jsonNode);
    }

    @Test
    public void getTpsServiceRutinerCallsgetTpsServiceRutinerOnGetTpsServiceRutinerService() {
        serviceController.getTpsServiceRutiner();

        verify(getTpsServiceRutinerService).exectue();
    }
}

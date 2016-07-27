package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.TpsServiceRutine;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpInternalServerErrorException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.DefaultAuthorisationService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.DefaultTpsServiceRutineService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class ServiceControllerTest {

    private static final List<TpsServiceRutine> SERVICES = mock(List.class);

    private static final String FNR                 = "12345678910";
    private static final String SERVICE_RUTINE_NAME = "serviceRutineName";
    
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
    DefaultAuthorisationService defaultAuthorisationServiceMock;

    @Mock
    Map<String, Object> parametersMock;

    @InjectMocks
    ServiceController serviceController;

    @Before
    public void setUp() {
        when(parametersMock.get(eq("fnr"))).thenReturn(FNR);
        when(getTpsServiceRutinerService.exectue()).thenReturn(SERVICES);
    }

    @Test
    public void getServiceAuthorisesTheUserBeforeContactingTps() throws Exception {
        when(defaultAuthorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString()))
                .thenReturn(true);

        serviceController.getService(mock(HttpSession.class), ENVIRONMENT_U, parametersMock, SERVICE_RUTINE_NAME);

        InOrder inOrder = inOrder(defaultGetTpsServiceRutineServiceMock, defaultAuthorisationServiceMock);

        inOrder.verify(defaultAuthorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString());
        inOrder.verify(defaultGetTpsServiceRutineServiceMock).execute(anyString(), eq(parametersMock), anyString());
    }

    @Test(expected = HttpUnauthorisedException.class)
    public void getServiceDeniesAccessIfUserIsUnauthorised() throws Exception {
        when(defaultAuthorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString()))
                .thenReturn(false);

        serviceController.getService(mock(HttpSession.class), ENVIRONMENT_U, parametersMock, SERVICE_RUTINE_NAME);

        verify(defaultAuthorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString());
        verify(defaultGetTpsServiceRutineServiceMock, never()).execute(anyString(), eq(parametersMock), anyString());
    }

    @Test
    public void environmentIsSentToTheService() throws Exception {
        when(defaultAuthorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString()))
                .thenReturn(true);

        serviceController.getService(mock(HttpSession.class), ENVIRONMENT_U, parametersMock, SERVICE_RUTINE_NAME);
        verify(defaultAuthorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), eq(ENVIRONMENT_U));
        verify(defaultGetTpsServiceRutineServiceMock).execute(anyString(), any(Map.class), eq(ENVIRONMENT_U));
    }

    @Test
    public void getTpsServiceRutinerReturnsServices() {
        Collection<TpsServiceRutine> services = serviceController.getTpsServiceRutiner();

        assertThat(services, is(equalTo(SERVICES)));
    }

    @Test(expected = HttpInternalServerErrorException.class)
    public void internalServerErrorIsThrownIfAnExceptionIsEncountered() throws Exception {
        when(defaultAuthorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString()))
                .thenReturn(true);
        when(defaultGetTpsServiceRutineServiceMock.execute(eq(SERVICE_RUTINE_NAME), anyMap(), eq(ENVIRONMENT_U)))
                .thenThrow(JMSException.class);

        serviceController.getService(mock(HttpSession.class), ENVIRONMENT_U, parametersMock, SERVICE_RUTINE_NAME);
    }

    @Test
    public void getTpsServiceRutinerCallsgetTpsServiceRutinerOnGetTpsServiceRutinerService() {
        serviceController.getTpsServiceRutiner();

        verify(getTpsServiceRutinerService).exectue();
    }
}

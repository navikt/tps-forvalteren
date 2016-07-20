package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.tps.vedlikehold.domain.service.User;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.AuthorisationService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.DefaultTpsServiceRutineService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class ServiceControllerTest {

    private static final String SERVICES = "SERVICES";

    @Mock
    GetTpsServiceRutinerService getTpsServiceRutinerService;

    @Mock
    UserContextHolder userContextHolderMock;

    @Mock
    DefaultTpsServiceRutineService defaultGetTpsServiceRutineServiceMock;

    @Mock
    AuthorisationService authorisationServiceMock;

    @Mock
    Map<String, Object> parametersMock;

    @InjectMocks
    ServiceController serviceController;

    @Before
    public void setUp() {
        when(parametersMock.get(eq("fnr"))).thenReturn("a number");
        when(getTpsServiceRutinerService.exectue()).thenReturn(SERVICES);
    }

    @Test
    public void getServiceAuthorisesTheUserBeforeContactingTps() throws Exception {
        when(authorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString()))
                .thenReturn(true);

        serviceController.getService(mock(HttpSession.class), "environment", parametersMock, "serviceRutineName");

        InOrder inOrder = inOrder(defaultGetTpsServiceRutineServiceMock, authorisationServiceMock);

        inOrder.verify(authorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString());
        inOrder.verify(defaultGetTpsServiceRutineServiceMock).execute(anyString(), eq(parametersMock), anyString());
    }

    @Test(expected = HttpUnauthorisedException.class)
    public void getServiceDeniesAccessIfUserIsUnauthorised() throws Exception {
        when(authorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString()))
                .thenReturn(false);

        serviceController.getService(mock(HttpSession.class), "environment", parametersMock, "serviceRutineName");

        verify(authorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString());
        verify(defaultGetTpsServiceRutineServiceMock, never()).execute(anyString(), eq(parametersMock), anyString());
    }

    @Test
    public void getServiceAllowsAccessIfNoFnrIsProvided() throws Exception {
        serviceController.getService(mock(HttpSession.class), "environment", mock(Map.class), "serviceRutineName");

        verify(authorisationServiceMock, never()).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString());
        verify(defaultGetTpsServiceRutineServiceMock).execute(anyString(), anyMap(), anyString());
    }

    @Test
    public void environmentsInUAreMappedToT4() throws Exception {
        when(authorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString()))
                .thenReturn(true);

        serviceController.getService(mock(HttpSession.class), "u1", parametersMock, "serviceRutineName");

        verify(authorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), eq("t4"));
        verify(defaultGetTpsServiceRutineServiceMock).execute(anyString(), any(Map.class), eq("t4"));
    }

    @Test
    public void environmentsNotInUAreNotMapped() throws Exception {
        when(authorisationServiceMock.userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), anyString()))
                .thenReturn(true);

        serviceController.getService(mock(HttpSession.class), "t1", parametersMock, "serviceRutineName");
        verify(authorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), eq("t1"));
        verify(defaultGetTpsServiceRutineServiceMock).execute(anyString(), any(Map.class), eq("t1"));

        serviceController.getService(mock(HttpSession.class), "q1", parametersMock, "serviceRutineName");
        verify(authorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), eq("q1"));
        verify(defaultGetTpsServiceRutineServiceMock).execute(anyString(), any(Map.class), eq("q1"));

        serviceController.getService(mock(HttpSession.class), "p1", parametersMock, "serviceRutineName");
        verify(authorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), eq("p1"));
        verify(defaultGetTpsServiceRutineServiceMock).execute(anyString(), any(Map.class), eq("p1"));

        serviceController.getService(mock(HttpSession.class), "", parametersMock, "serviceRutineName");
        verify(authorisationServiceMock).userIsAuthorisedToReadPersonInEnvironment(any(User.class), anyString(), eq(""));
        verify(defaultGetTpsServiceRutineServiceMock).execute(anyString(), any(Map.class), eq(""));

    }

    @Test
    public void getTpsServiceRutinerReturnsServices() {
        String services = serviceController.getTpsServiceRutiner();

        assertThat(services, is(equalTo(SERVICES)));
    }

    @Test
    public void getTpsServiceRutinerCallsgetTpsServiceRutinerOnGetTpsServiceRutinerService() {
        serviceController.getTpsServiceRutiner();

        verify(getTpsServiceRutinerService).exectue();
    }
}

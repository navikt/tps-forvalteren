package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import no.nav.tps.forvalteren.domain.service.environment.Environment;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;
import no.nav.tps.forvalteren.service.user.UserContextHolder;


@RunWith(MockitoJUnitRunner.class)
public class EnvironmentControllerTest {

    private static final String SESSION_ID = "sessionID";
    private static final Set<String> ENVIRONMENTS_Q = new HashSet<>( Arrays.asList(   "q4", "q9") );
    private static final Set<String> ENVIRONMENTS_T = new HashSet<>( Arrays.asList(   "t3", "t4", "t7") );
    private static final Set<String> ENVIRONMENTS_U = new HashSet<>( Arrays.asList(   "u1","u5", "u6") );
    private static final String ENVIRONMENT_PROD = "p";

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private GetEnvironments getEnvironmentsCommandMock;

    @Mock
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironmentMock;

    @Mock
    private UserContextHolder userContextHolder;

    @InjectMocks
    private EnvironmentController controller;

    @Before
    public void setUp() {
        when( getEnvironmentsCommandMock.getEnvironments() ).thenReturn(ENVIRONMENTS_Q);
    }

    @Test
    public void getEnvironmentsReturnsOnlySupportedQEnvironments() {
        ReflectionTestUtils.setField(controller, "currentEnvironmentIsProd", false);
        when(filterEnvironmentsOnDeployedEnvironmentMock.execute(anySet())).thenReturn(ENVIRONMENTS_Q);

        Environment environment = controller.getEnvironments();

        assertThat(environment.getEnvironments(), containsInAnyOrder("q4", "q9"));
        assertThat(environment.isProductionMode(), is(false));
    }

    @Test
    public void getEnvironmentsReturnsOnlyQEnvironmentsWhenIQEnvironment() {
        ReflectionTestUtils.setField(controller, "currentEnvironmentIsProd", false);
        when(filterEnvironmentsOnDeployedEnvironmentMock.execute(anySet())).thenReturn(ENVIRONMENTS_T);

        Environment environment = controller.getEnvironments();

        assertThat(environment.getEnvironments(), containsInAnyOrder("t3", "t4","t7"));
        assertThat(environment.isProductionMode(), is(false));
    }

    @Test
    public void getEnvironmentsReturnsOnlyUEnvironmentsWhenInUEnvironment() {
        ReflectionTestUtils.setField(controller, "currentEnvironmentIsProd", false);
        when(filterEnvironmentsOnDeployedEnvironmentMock.execute(anySet())).thenReturn(ENVIRONMENTS_U);

        Environment environment = controller.getEnvironments();

        assertThat(environment.getEnvironments(), containsInAnyOrder("u1", "u5", "u6"));
        assertThat(environment.isProductionMode(), is(false));
    }

    @Test
    public void getEnvironmentReturnsOnlyProdEnvironmentAsSupportedEnvironmentIfCurrentEnvironmentIsProd(){
        ReflectionTestUtils.setField(controller, "currentEnvironmentIsProd", true);
        when(filterEnvironmentsOnDeployedEnvironmentMock.execute(anySet())).thenReturn(new HashSet<>( Arrays.asList( ENVIRONMENT_PROD)));
        Environment environment = controller.getEnvironments();
        assertThat(environment.getEnvironments(), contains("p"));
        assertThat(environment.getEnvironments(), hasSize(1));
        assertThat(environment.isProductionMode(), is(true));
    }
}

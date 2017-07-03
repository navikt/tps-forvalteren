package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.vera.GetEnvironments;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class EnvironmentControllerTest {

    private static final String SESSION_ID = "sessionID";
    private static final Set<String> ENVIRONMENTS_Q = new HashSet<>( Arrays.asList(   "q4", "q9", "t3", "t4", "t7") );
    private static final Set<String> ENVIRONMENTS_T = new HashSet<>( Arrays.asList(   "q4", "q9", "t3", "t4", "t7", "u1","u5", "u6") );
    private static final Set<String> ENVIRONMENTS_U = new HashSet<>( Arrays.asList(    "t3", "t4", "t7", "u1","u5", "u6") );
    private static final String ENVIRONMENT_PROD = "p";
    private static final String ENVIRONMENT_PROD_VALUE = "currentEnvironmentIsProd";

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    public GetEnvironments getEnvironmentsCommandMock;

    @Mock
    public FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironmentMock;

    @InjectMocks
    private EnvironmentController controller;

    @Before
    public void setUp() {
        when( getEnvironmentsCommandMock.getEnvironmentsFromVera("tpsws") ).thenReturn(ENVIRONMENTS_Q);
        when( httpSessionMock.getId() ).thenReturn(SESSION_ID);
        ReflectionTestUtils.setField(controller, ENVIRONMENT_PROD_VALUE, false);
    }

    @Test
    public void getEnvironmentsReturnsOnlySupportedEnvironments() {
        ReflectionTestUtils.setField(controller, ENVIRONMENT_PROD_VALUE, false);

        when(filterEnvironmentsOnDeployedEnvironmentMock.execute(anySet())).thenReturn(ENVIRONMENTS_Q);

        Set<String> environments = controller.getEnvironments();

        assertThat(environments, containsInAnyOrder("q4", "q9", "t3", "t4","t7"));
    }

    @Test
    public void getEnvironmentsReturnsOnlyQEnvironmentsWhenIQEnvironment() {
        ReflectionTestUtils.setField(controller, ENVIRONMENT_PROD_VALUE, false);

        when(filterEnvironmentsOnDeployedEnvironmentMock.execute(anySet())).thenReturn(ENVIRONMENTS_T);

        Set<String> environments = controller.getEnvironments();

        assertThat(environments, containsInAnyOrder("q4", "q9", "t3", "t4","t7", "u1","u5", "u6"));
    }

    @Test
    public void getEnvironmentReturnsOnlyProdEnvironmentAsSupportedEnvironmentIfCurrentEnvironmentIsProd(){
        ReflectionTestUtils.setField(controller, ENVIRONMENT_PROD_VALUE, true);
        Set<String> environments = controller.getEnvironments();
        assertThat(environments, contains(ENVIRONMENT_PROD));
        assertThat(environments, hasSize(1));
    }

}

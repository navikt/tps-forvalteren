package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.tps.vedlikehold.service.command.vera.GetEnvironments;
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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.eventFrom;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class EnvironmentControllerTest {

    private static final String SESSION_ID = "sessionID";
    private static final Set<String> ENVIRONMENTS = new HashSet<>( Arrays.asList(  "p", "q4", "t3", "t4", "t7", "u1","u5", "u6") );
    private static final String ENVIRONMENT_PROD = "p";
    private static final String ENVIRONMENT_PROPERTY_VALUE = "currentEnvironmentIsProd";

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    public GetEnvironments getEnvironmentsCommandMock;

    @InjectMocks
    private EnvironmentController controller;

    @Before
    public void setUp() {
        when( getEnvironmentsCommandMock.execute("tpsws") ).thenReturn(ENVIRONMENTS);
        when( httpSessionMock.getId() ).thenReturn(SESSION_ID);
        ReflectionTestUtils.setField(controller, ENVIRONMENT_PROPERTY_VALUE, false);
    }

    @Test
    public void getEnvironmentsReturnsOnlySupportedEnvironments() {
        Set<String> environments = controller.getEnvironments();

        assertThat(environments, containsInAnyOrder("t3",  "t4", "u1", "u5", "u6"));
    }

    @Test
    public void getEnvironmentReturnsOnlyProdEnvironmentAsSupportedEnvironmentIfCurrentEnvironmentIsProd(){
        ReflectionTestUtils.setField(controller, ENVIRONMENT_PROPERTY_VALUE, true);
        Set<String> environments = controller.getEnvironments();
        assertThat(environments, contains(ENVIRONMENT_PROD));
        assertThat(environments, hasSize(1));
    }

}

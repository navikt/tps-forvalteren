package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpSession;
import no.nav.tps.vedlikehold.service.command.vera.GetEnvironments;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class EnvironmentControllerTest {

    private static final String SESSION_ID = "sessionID";
    private static final Set<String> ENVIRONMENTS = new HashSet<>( Arrays.asList("p", "q4", "t3", "u1") );

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    public GetEnvironments getEnvironments;

    @InjectMocks
    private EnvironmentController controller;

    @Before
    public void setUp() {
        when( getEnvironments.execute("tpsws") ).thenReturn(ENVIRONMENTS);
        when( httpSessionMock.getId() ).thenReturn(SESSION_ID);
    }

    @Test
    public void getEnvironmentsReturnsOnlySupportedEnvironments() {
        Set<String> environments = controller.getEnvironments();

        assertThat(environments, containsInAnyOrder("t3", "u1"));
    }

}

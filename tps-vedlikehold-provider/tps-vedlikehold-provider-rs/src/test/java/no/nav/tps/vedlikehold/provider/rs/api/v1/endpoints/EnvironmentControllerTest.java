package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.tps.vedlikehold.consumer.rs.vera.VeraConsumer;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.EnvironmentController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class EnvironmentControllerTest {

    private static final String SESSION_ID = "sessionID";
    private static final Set<String> ENVIRONMENTS = new HashSet<>( Arrays.asList("p", "q4", "t3") );

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private VeraConsumer veraConsumerMock;

    @InjectMocks
    private EnvironmentController controller;

    @Before
    public void setUp() {
        when( veraConsumerMock.getEnvironments("tpsws") ).thenReturn(ENVIRONMENTS);
        when( httpSessionMock.getId() ).thenReturn(SESSION_ID);
    }

    @Test
    public void getEnvironmentsReturnsEnvironmentList() {
        Set<String> environments = controller.getEnvironments(httpSessionMock);

        assertThat(environments, is(ENVIRONMENTS));
    }

}

package no.nav.tps.vedlikehold.consumer.rs.vera;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class VeraConsumerTest {

    private static final String VERA_DOES_NOT_ANSWER_ERROR = "Vera does not answer";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private RestTemplate restTemplateMock;

    @InjectMocks
    private VeraConsumer veraConsumer = new VeraConsumer();

    private VeraApplication q4;
    private VeraApplication t3;
    private VeraApplication p;

    @Before
    public void before() {
        q4 = new VeraApplication();
        t3 = new VeraApplication();
         p = new VeraApplication();

        q4.setEnvironment("q4");
        t3.setEnvironment("t3");
        p.setEnvironment("p");
    }

    @Test
    public void getEnvironmentsReturnsEmptyListIfNoEnvironmentsAreFound() {
        VeraApplication[] returnedApplications = new VeraApplication[]{};

        when( restTemplateMock.getForObject(anyString(), anyObject()) ).thenReturn(returnedApplications);

        assertThat(veraConsumer.getEnvironments("tpsws"), hasSize(0));
    }

    @Test
    public void getEnvironmentsReturnsListWithOneEnvironment() {
        VeraApplication[] returnedApplications = new VeraApplication[]{q4};

        when( restTemplateMock.getForObject(anyString(), anyObject()) ).thenReturn(returnedApplications);

        List<String> response = Arrays.asList("q4");

        assertThat(veraConsumer.getEnvironments("tpsws"), hasItems("q4"));
        assertThat(veraConsumer.getEnvironments("tpsws"), hasSize(response.size()));
    }

    @Test
    public void getEnvironmentsReturnsListWithAllEnvironments() {
        VeraApplication[] returnedApplications = new VeraApplication[]{p, q4, t3};

        when( restTemplateMock.getForObject(anyString(), anyObject()) ).thenReturn(returnedApplications);

        assertThat(veraConsumer.getEnvironments("tpsws"), containsInAnyOrder("p", "q4", "t3"));
    }

    @Test
    public void pingReturnsTrueWhenVeraRespondsNormally() throws Exception {

        VeraApplication[] returnedApplications = new VeraApplication[]{p, q4, t3};

        when( restTemplateMock.getForObject(anyString(), anyObject()) ).thenReturn(returnedApplications);

        boolean result = veraConsumer.ping();

        assertThat(result, is(true));
    }

    @Test
    public void pingThrowsExceptionWhenVeraThrowsException() throws Exception {

        RuntimeException thrownException = new RuntimeException(VERA_DOES_NOT_ANSWER_ERROR);

        when( restTemplateMock.getForObject(anyString(), anyObject()) ).thenThrow(thrownException);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(VERA_DOES_NOT_ANSWER_ERROR);

        boolean result = veraConsumer.ping();

        assertThat(result, is(false));
    }
}

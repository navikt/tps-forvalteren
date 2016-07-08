package no.nav.tps.vedlikehold.consumer.rs.vera;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class VeraConsumerTest {
    private VeraApplication[] returnedApplications;

    @Mock
    private RestTemplate restTemplateMock;

    @InjectMocks
    private VeraConsumer veraConsumer = new VeraConsumer();

    private VeraApplication q4 = new VeraApplication();
    private VeraApplication t3 = new VeraApplication();
    private VeraApplication p = new VeraApplication();

    @Before
    public void before() {
        q4.setEnvironment("q4");
        t3.setEnvironment("t3");
        p.setEnvironment("p");
    }

    @Test
    public void getEnvironmentsReturnsEmptyListIfNoEnvironmentsAreFound() {
        returnedApplications = new VeraApplication[]{};

        when( restTemplateMock.getForObject(anyString(), anyObject()) ).thenReturn(returnedApplications);

        assertThat(veraConsumer.getEnvironments("tpsws"), hasSize(0));
    }

    @Test
    public void getEnvironmentsReturnsListWithOneEnvironment() {
        returnedApplications = new VeraApplication[]{q4};

        when( restTemplateMock.getForObject(anyString(), anyObject()) ).thenReturn(returnedApplications);

        List<String> response = Arrays.asList("q4");

        assertThat(veraConsumer.getEnvironments("tpsws"), contains("q4"));
        assertThat(veraConsumer.getEnvironments("tpsws"), hasSize(response.size()));
    }


    @Test
    public void getEnvironmentsReturnsListWithAllEnvironments() {
        returnedApplications = new VeraApplication[]{p, q4, t3};

        when( restTemplateMock.getForObject(anyString(), anyObject()) ).thenReturn(returnedApplications);

        assertThat(veraConsumer.getEnvironments("tpsws"), containsInAnyOrder("p", "q4", "t3"));
    }
}

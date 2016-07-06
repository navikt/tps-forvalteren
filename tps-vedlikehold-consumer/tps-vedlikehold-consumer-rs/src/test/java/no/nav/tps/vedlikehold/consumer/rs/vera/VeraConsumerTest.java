package no.nav.tps.vedlikehold.consumer.rs.vera;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
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
    public void veraConsumerListEnvsReturnsEmptyListIfNoEnvironmentsAreFound() {
        returnedApplications = new VeraApplication[]{};

        when( restTemplateMock.getForObject(anyString(), anyObject()) ).thenReturn(returnedApplications);

        List<String> response = new ArrayList<String>();

        assertThat( "empty array is returned",  veraConsumer.listEnvs().equals(response));
    }

    @Test
    public void veraConsumerListEnvsReturnsListWithOneEnvironment() {
        returnedApplications = new VeraApplication[]{q4};

        when( restTemplateMock.getForObject(anyString(), anyObject()) ).thenReturn(returnedApplications);

        List<String> response = Arrays.asList("q4");

        assertThat( "array containing the q4 is returned",  veraConsumer.listEnvs().equals(response));
    }


    @Test
    public void veraConsumerListEnvsReturnsListWithAllEnvironments() {
        returnedApplications = new VeraApplication[]{p, q4, t3};

        when( restTemplateMock.getForObject(anyString(), anyObject()) ).thenReturn(returnedApplications);

        List<String> response = Arrays.asList("p", "q4", "t3");

        assertThat( "array containing all environments is returned",  veraConsumer.listEnvs().equals(response));
    }

    @Test
    public void veraConsumerListEnvsReturnsSortedListWithAllEnvironments() {
        returnedApplications = new VeraApplication[]{q4, t3, p};

        when( restTemplateMock.getForObject(anyString(), anyObject()) ).thenReturn(returnedApplications);

        List<String> response =  Arrays.asList("p", "q4", "t3");

        assertThat( "the array returned is sorted",  veraConsumer.listEnvs().equals(response));
    }
}

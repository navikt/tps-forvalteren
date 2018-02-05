package no.nav.tps.forvalteren.consumer.rs.environments;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;


@RunWith(MockitoJUnitRunner.class)
public class FetchEnvironmentsManagerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private RestTemplate restTemplateMock;

    @InjectMocks
    private FetchEnvironmentsConsumer consumer = new FetchEnvironmentsManager();

    private FetchEnvironmentsApplication q4;
    private FetchEnvironmentsApplication t3;
    private FetchEnvironmentsApplication p;

    @Before
    public void before() {
        q4 = new FetchEnvironmentsApplication ();
        t3 = new FetchEnvironmentsApplication ();
        p = new FetchEnvironmentsApplication ();

        q4.setEnvironment("q4");
        t3.setEnvironment("t3");
        p.setEnvironment("p");
    }

    @Test
    public void getEnvironmentsReturnsEmptyListIfNoEnvironmentsAreFound() {
        FetchEnvironmentsApplication[] returnedApplications = new FetchEnvironmentsApplication[]{};

        when(restTemplateMock.getForObject(anyString(), anyObject())).thenReturn(returnedApplications);

        assertThat(consumer.getEnvironments("tpsws"), hasSize(0));
    }

    @Test
    public void getEnvironmentsReturnsListWithOneEnvironment() {
        FetchEnvironmentsApplication[] returnedApplications = new FetchEnvironmentsApplication[]{q4};

        when(restTemplateMock.getForObject(anyString(), anyObject())).thenReturn(returnedApplications);

        List<String> response = Arrays.asList("q4");

        assertThat(consumer.getEnvironments("tpsws"), hasItems("q4"));
        assertThat(consumer.getEnvironments("tpsws"), hasSize(response.size()));
    }

    @Test
    public void getEnvironmentsReturnsListWithAllEnvironments() {
        FetchEnvironmentsApplication[] returnedApplications = new FetchEnvironmentsApplication[]{p, q4, t3};

        when(restTemplateMock.getForObject(anyString(), anyObject())).thenReturn(returnedApplications);

        assertThat(consumer.getEnvironments("tpsws"), containsInAnyOrder("p", "q4", "t3"));
    }

}

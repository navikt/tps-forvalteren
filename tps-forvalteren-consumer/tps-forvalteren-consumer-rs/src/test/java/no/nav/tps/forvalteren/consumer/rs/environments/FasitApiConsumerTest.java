package no.nav.tps.forvalteren.consumer.rs.environments;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitApplication;

@RunWith(MockitoJUnitRunner.class)
public class FasitApiConsumerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MapperFacade mapperFacade;

    @Mock
    private ResponseEntity<FasitApplication[]> applications;

    @InjectMocks
    private FasitApiConsumer consumer;

    private FasitApplication q4;
    private FasitApplication t3;
    private FasitApplication p;

    @Before
    public void before() {
        q4 = new FasitApplication();
        t3 = new FasitApplication();
        p = new FasitApplication();

        q4.setEnvironment("q4");
        t3.setEnvironment("t3");
        p.setEnvironment("p");

        when(restTemplate.getForEntity(anyString(), eq(FasitApplication[].class))).thenReturn(applications);
    }

    @Test
    public void getEnvironmentsReturnsEmptyListIfNoEnvironmentsAreFound() {
        when(applications.getBody()).thenReturn(new FasitApplication[0]);

        assertThat(consumer.getEnvironments("tpsws"), hasSize(0));
    }

    @Test
    public void getEnvironmentsReturnsListWithOneEnvironment() {

        when(applications.getBody()).thenReturn((FasitApplication[]) Arrays.asList(q4).toArray());

        List<String> response = Arrays.asList("q4");

        assertThat(consumer.getEnvironments("tpsws"), hasItems("q4"));
        assertThat(consumer.getEnvironments("tpsws"), hasSize(response.size()));
    }

    @Test
    public void getEnvironmentsReturnsListWithAllEnvironments() {
        when(applications.getBody()).thenReturn((FasitApplication[]) Arrays.asList(p, q4, t3).toArray());

        assertThat(consumer.getEnvironments("tpsws"), containsInAnyOrder("p", "q4", "t3"));
    }
}

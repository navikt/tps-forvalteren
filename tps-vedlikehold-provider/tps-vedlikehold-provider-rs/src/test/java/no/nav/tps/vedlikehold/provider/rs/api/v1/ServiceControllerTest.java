package no.nav.tps.vedlikehold.provider.rs.api.v1;

import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.ServiceController;
import no.nav.tps.vedlikehold.service.command.servicerutiner.GetTpsServiceRutinerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author Tobias Hansen (Visma Consulting AS).
 */

@RunWith(MockitoJUnitRunner.class)
public class ServiceControllerTest {

    private static final String SERVICES = "SERVICES";

    @Mock
    GetTpsServiceRutinerService getTpsServiceRutinerService;

    @InjectMocks
    private ServiceController serviceController;

    @Before
    public void setUp() {
        when(getTpsServiceRutinerService.getTpsServiceRutiner()).thenReturn(SERVICES);
    }

    @Test
    public void getTpsServiceRutinerReturnsServices() {
        String services = serviceController.getTpsServiceRutiner();

        assertThat(services, is(equalTo(SERVICES)));
    }

    @Test
    public void getTpsServiceRutinerCallsgetTpsServiceRutinerOnGetTpsServiceRutinerService() {
        serviceController.getTpsServiceRutiner();

        verify(getTpsServiceRutinerService).getTpsServiceRutiner();
    }
}
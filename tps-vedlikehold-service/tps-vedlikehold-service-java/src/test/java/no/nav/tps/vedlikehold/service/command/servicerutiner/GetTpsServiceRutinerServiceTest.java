package no.nav.tps.vedlikehold.service.command.servicerutiner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class GetTpsServiceRutinerServiceTest {

    private static final String SERVICES = "SERVICES";

    @Mock
    GetTpsServiceRutinerService getTpsServiceRutinerService;

    @Before
    public void setUp() {
        when(getTpsServiceRutinerService.getTpsServiceRutiner()).thenReturn(SERVICES);
    }

    @Test
    public void getTpsServiceRutinerReturnsServices() {
        String services = getTpsServiceRutinerService.getTpsServiceRutiner();

        assertThat(services, is(equalTo(SERVICES)));
    }
}
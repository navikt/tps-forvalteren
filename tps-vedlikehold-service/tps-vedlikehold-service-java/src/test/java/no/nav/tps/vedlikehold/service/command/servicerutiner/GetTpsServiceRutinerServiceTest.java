package no.nav.tps.vedlikehold.service.command.servicerutiner;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public class GetTpsServiceRutinerServiceTest {

    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    @Before
    public void setup() {
        getTpsServiceRutinerService = new DefaultGetTpsServiceRutinerService();
    }

    @Test
    public void getTpsServiceRutinerServiceReturnsString() {
        Object services = getTpsServiceRutinerService.exectue();

        assertThat(services instanceof String, is(true));
    }
}
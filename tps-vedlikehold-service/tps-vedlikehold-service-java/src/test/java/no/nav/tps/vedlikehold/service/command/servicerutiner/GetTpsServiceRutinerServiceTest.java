package no.nav.tps.vedlikehold.service.command.servicerutiner;

import no.nav.tps.vedlikehold.service.command.config.CommandConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CommandConfig.class)
public class GetTpsServiceRutinerServiceTest {

    @Autowired
    GetTpsServiceRutinerService getTpsServiceRutinerService;

    @Test
    public void getTpsServiceRutinerServiceReturnsString() {
        Object services = getTpsServiceRutinerService.exectue();

        assertThat(services instanceof String, is(true));
    }
}
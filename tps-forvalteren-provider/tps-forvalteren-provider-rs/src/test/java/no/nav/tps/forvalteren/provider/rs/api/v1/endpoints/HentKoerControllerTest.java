package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import no.nav.tps.forvalteren.service.command.tps.xmlmelding.GetQueuesFromEnvironment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HentKoerControllerTest {

    @Mock
    private GetQueuesFromEnvironment getQueuesFromEnvironment;

    @InjectMocks
    private HentKoerController hentKoerController;

    @Test
    public void getQueuesTest() {
        hentKoerController.getQueues();

        verify(getQueuesFromEnvironment).execute();
    }

}

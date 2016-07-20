package no.nav.tps.vedlikehold.service.command.fasit;

import no.nav.tps.vedlikehold.consumer.ws.fasit.FasitClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * @author Kristian Kyvik (Visma Consulting).
 */
@RunWith(MockitoJUnitRunner.class)
public class PingFasitTest {
    @Mock
    private FasitClient consumerMock;

    @InjectMocks
    private PingFasit command;

    @Test
    public void callsPingOnConsumerWhenExecuting() throws Exception{
        command.execute();

        verify(consumerMock).ping();
    }
}

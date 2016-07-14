package no.nav.tps.vedlikehold.service.command.tpsws;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.DefaultEgenAnsattConsumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class PingEgenAnsattTest {
    @Mock
    private DefaultEgenAnsattConsumer consumerMock;

    @InjectMocks
    private PingEgenAnsatt command;

    @Test
    public void callsPingOnConsumerWhenExecuting() throws Exception{
        command.execute();

        verify(consumerMock).ping();
    }
}

package no.nav.tps.vedlikehold.service.command.mq;

import no.nav.tps.vedlikehold.consumer.mq.services.MessageQueueService;
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
public class PingMqTest {
    @Mock
    private MessageQueueService consumerMock;

    @InjectMocks
    private PingMq command;

    @Test
    public void callsPingOnConsumerWhenExecuting() throws Exception{
        command.execute();

        verify(consumerMock).ping();
    }
}


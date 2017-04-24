package no.nav.tps.forvalteren.service.command.mq;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class PingMqTest {
    @Mock
    private MessageQueueConsumer consumerMock;

    @InjectMocks
    private PingMq command;

    @Test
    public void callsPingOnConsumerWhenExecuting() throws Exception{
        command.execute();

        verify(consumerMock).ping();
    }
}


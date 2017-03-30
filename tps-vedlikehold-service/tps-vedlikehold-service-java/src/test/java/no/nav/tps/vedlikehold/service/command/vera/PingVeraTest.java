package no.nav.tps.vedlikehold.service.command.vera;

import no.nav.tps.vedlikehold.consumer.rs.vera.VeraConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class PingVeraTest {
    @Mock
    private VeraConsumer consumerMock;

    @InjectMocks
    private PingVera command;

    @Test
    public void callsPingOnConsumerWhenExecuting() throws Exception{
        command.execute();

        verify(consumerMock).ping();
    }
}



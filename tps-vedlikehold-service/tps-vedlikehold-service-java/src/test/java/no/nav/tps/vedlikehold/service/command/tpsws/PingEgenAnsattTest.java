package no.nav.tps.vedlikehold.service.command.tpsws;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class PingEgenAnsattTest {
    @Mock
    private EgenAnsattConsumer consumerMock;

    @InjectMocks
    private PingEgenAnsatt command;

    @Test
    public void callsPingOnConsumerWhenExecuting() throws Exception{
        command.execute();

        verify(consumerMock).ping();
    }
}

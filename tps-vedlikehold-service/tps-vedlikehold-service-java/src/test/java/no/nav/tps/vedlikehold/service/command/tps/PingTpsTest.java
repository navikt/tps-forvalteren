package no.nav.tps.vedlikehold.service.command.tps;

import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@RunWith(MockitoJUnitRunner.class)
public class PingTpsTest {

    private static final String PING_MESSAGE = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData xmlns=\"http://www.rtv.no/NamespaceTPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.rtv.no/NamespaceTPS H:\\SYSTEM~1\\SYSTEM~4\\FS03TP~1\\TPSDAT~1.XSD\"><tpsServiceRutine><serviceRutinenavn>FS03-OTILGANG-TILSRTPS-O</serviceRutinenavn></tpsServiceRutine></tpsPersonData>";


    @Mock
    private MessageQueueConsumer consumerMock;

    @InjectMocks
    private PingTps command;

    @Test
    public void callsPingOnConsumerWhenExecuting() throws Exception{
        when(consumerMock.sendMessage(anyString())).thenReturn("TPS OK");
        command.execute();

        verify(consumerMock).sendMessage(eq(PING_MESSAGE));
    }

    @Test(expected = RuntimeException.class)
    public void throwsExceptionIfResponseIsEmpty() throws Exception{
        when(consumerMock.sendMessage(anyString())).thenReturn("");
        command.execute();

        verify(consumerMock).sendMessage(eq(PING_MESSAGE));
    }

    @Test(expected = RuntimeException.class)
    public void throwsExceptionIfResponseDoesNotContainTpsOk() throws Exception{
        when(consumerMock.sendMessage(anyString())).thenReturn("This is an unexpected status");
        command.execute();

        verify(consumerMock).sendMessage(eq(PING_MESSAGE));
    }
}

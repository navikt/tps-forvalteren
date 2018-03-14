package no.nav.tps.forvalteren.service.command.tps;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageFixedQueueServiceFactory;
import no.nav.tps.forvalteren.domain.rs.RsXmlMelding;
import no.nav.tps.forvalteren.service.command.tps.xmlmelding.TpsXmlSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TpsXmlSenderTest {

    private String melding = "test_melding";
    private String ko = "QA.D8_411.TPS_FORESPORSEL_XML_O";

    private RsXmlMelding rsXmlMelding = new RsXmlMelding(melding, ko);

    @Mock
    private MessageFixedQueueServiceFactory messageFixedQueueServiceFactory;

    @Mock
    private MessageQueueConsumer messageQueueConsumer;

    @InjectMocks
    private TpsXmlSender tpsXmlSender;

    @Before
    public void setup() throws Exception {
        when(messageFixedQueueServiceFactory.createMessageQueueConsumerWithFixedQueueName("D8", ko))
                .thenReturn(messageQueueConsumer);
        when(messageQueueConsumer.sendMessage(rsXmlMelding.getMelding())).thenReturn("");
    }

    @Test
    public void tpsXmlSenderTest() throws Exception {
        tpsXmlSender.sendXml(rsXmlMelding);

        verify(messageQueueConsumer).sendMessage(rsXmlMelding.getMelding());
    }
}

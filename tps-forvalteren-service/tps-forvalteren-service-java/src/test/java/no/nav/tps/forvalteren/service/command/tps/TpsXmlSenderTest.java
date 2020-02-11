package no.nav.tps.forvalteren.service.command.tps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageFixedQueueServiceFactory;
import no.nav.tps.forvalteren.domain.rs.RsTpsMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.utils.ContainsXmlElements;
import no.nav.tps.forvalteren.service.command.tps.xmlmelding.TpsXmlSender;

@RunWith(MockitoJUnitRunner.class)
public class TpsXmlSenderTest {
    private String miljoe = "D8";
    private String melding = "test_melding";
    private String ko = "QA.D8_411.TPS_FORESPORSEL_XML_O";
    private Long timeout = 5L;
    private StringBuilder rsTpsMeldingMedHeader = new StringBuilder("header " + melding);
    private RsTpsMelding rsTpsMelding = new RsTpsMelding(miljoe, melding, ko, timeout);

    @Mock
    private MessageFixedQueueServiceFactory messageFixedQueueServiceFactory;

    @Mock
    private SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;

    @Mock
    private MessageQueueConsumer messageQueueConsumer;

    @Mock
    private ContainsXmlElements containsXmlElements;

    @InjectMocks
    private TpsXmlSender tpsXmlSender;

    @Before
    public void setup() throws Exception {

        when(containsXmlElements.execute(anyString())).thenReturn(false);
        when(skdAddHeaderToSkdMelding.execute(any())).thenReturn(rsTpsMeldingMedHeader);
        when(messageFixedQueueServiceFactory.createMessageQueueConsumerWithFixedQueueName("D8", ko))
                .thenReturn(messageQueueConsumer);
    }

    @Test
    public void tpsXmlSenderTest() throws Exception {
        tpsXmlSender.sendTpsMelding(rsTpsMelding);

        verify(messageQueueConsumer).sendMessage(rsTpsMelding.getMelding(), rsTpsMelding.getTimeout() * 1000L);
    }
}

package no.nav.tps.forvalteren.service.command.tps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsRequestConfig;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.authorisation.ForbiddenCallHandlerService;

@RunWith(MockitoJUnitRunner.class)
public class SkdMeldingMQConsumerTest {

    private TpsSkdRequestMeldingDefinition skdMeldingDefinition = new TpsSkdRequestMeldingDefinition();
    private TpsRequestConfig config = new TpsRequestConfig();
    private String REQUEST_QUEUE_TEST = "testQ";

    @Mock
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Mock
    private MessageQueueConsumer messageQueueConsumer;

    @Mock
    private ForbiddenCallHandlerService forbiddenCallHandlerService;
    
    @InjectMocks
    private SkdMeldingMQConsumer skdMeldingMQConsumer;

    @Before
    public void setup() throws Exception {
        config.setRequestQueue(REQUEST_QUEUE_TEST);
        skdMeldingDefinition.setConfig(config);

        when(messageQueueServiceFactory.createMessageQueueConsumer(any(), any(), eq(false))).thenReturn(messageQueueConsumer);
    }

    @Test
    public void callsAuthorisationService() throws Exception {
        skdMeldingMQConsumer.sendMessage("test", skdMeldingDefinition, "test");

        verify(forbiddenCallHandlerService, never()).authoriseRestCall(skdMeldingDefinition);
    }
}
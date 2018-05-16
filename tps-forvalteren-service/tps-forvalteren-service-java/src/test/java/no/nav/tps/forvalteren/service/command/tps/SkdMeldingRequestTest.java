package no.nav.tps.forvalteren.service.command.tps;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsRequestConfig;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.authorisation.ForbiddenCallHandlerService;

@RunWith(MockitoJUnitRunner.class)
public class SkdMeldingRequestTest {

    private TpsSkdRequestMeldingDefinition skdMeldingDefinition = new TpsSkdRequestMeldingDefinition();
    private TpsRequestConfig config = new TpsRequestConfig();
    private String REQUEST_QUEUE_TEST = "testQ";

    @Mock
    private MessageQueueServiceFactory messageQueueServiceFactoryMock;

    @Mock
    private MessageQueueConsumer messageQueueConsumerMock;

    @Mock
    private ForbiddenCallHandlerService ForbiddenCallHandlerServiceMock;
    
    @InjectMocks
    private SkdMeldingRequest skdMeldingRequest;

    @Before
    public void setup() throws Exception {
        config.setRequestQueue(REQUEST_QUEUE_TEST);
        skdMeldingDefinition.setConfig(config);

        when(messageQueueServiceFactoryMock.createMessageQueueConsumer(any(), any())).thenReturn(messageQueueConsumerMock);
    }

    @Test
    public void callsAuthorisationService() throws Exception {
        skdMeldingRequest.execute("test", skdMeldingDefinition, "test");

        verify(ForbiddenCallHandlerServiceMock).authoriseRestCall(skdMeldingDefinition);
    }
}
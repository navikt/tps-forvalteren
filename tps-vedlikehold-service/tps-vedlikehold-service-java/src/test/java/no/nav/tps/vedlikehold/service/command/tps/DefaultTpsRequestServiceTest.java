package no.nav.tps.vedlikehold.service.command.tps;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConstants;
import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultTpsRequestServiceTest {

    private static final String ENVIRONMENT         = "environment";
    private static final String RESPONSE_XML        = "<responses><response>response</response></responses>";

    @Mock
    private MessageQueueServiceFactory messageQueueServiceFactoryMock;

    @Mock
    private XmlMapper xmlMapperMock;

    @Mock
    private TpsRequestServiceRoutine tpsRequestServiceRoutineMock;

    @Mock
    private MessageQueueConsumer messageQueueConsumerMock;

    @Mock
    private TpsServiceRoutine tpsServiceRoutineMock;

    @Mock
    private Object responseObjectMock;

    @InjectMocks
    private DefaultTpsRequestService defaultGetTpsRequestService;

    @Before
    public void setUp() throws Exception {
        when(messageQueueServiceFactoryMock.createMessageQueueService(eq(ENVIRONMENT), eq(FasitConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS))).thenReturn(messageQueueConsumerMock);
        when(messageQueueConsumerMock.sendMessage(anyString()) ).thenReturn(RESPONSE_XML);

        when(tpsRequestServiceRoutineMock.getEnvironment()).thenReturn(ENVIRONMENT);
    }

    @Test
    public void createsMessageQueueServiceUsingTheCorrectEnvironment() throws Exception {
        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestServiceRoutineMock, tpsServiceRoutineMock);

        verify(messageQueueServiceFactoryMock).createMessageQueueService(eq(ENVIRONMENT), eq(FasitConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS));
    }

    @Test
    public void aMessageIsSentToTps() throws Exception {
        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestServiceRoutineMock, tpsServiceRoutineMock);
        verify(messageQueueConsumerMock).sendMessage(anyString());
    }

    @Test
    public void responseXmlIsProvided() throws Exception {
//        String result = defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestServiceRoutineMock, tpsServiceRoutineMock);
        Response result = defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestServiceRoutineMock, tpsServiceRoutineMock);
        assertThat(result.getXml(), is(RESPONSE_XML));
    }

    @Test(expected = JMSException.class)
    public void exceptionDuringMessageQueueServiceCreationFailsGracefully() throws Exception {
        when(messageQueueServiceFactoryMock.createMessageQueueService(anyString(), anyString())).thenThrow(JMSException.class);

        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestServiceRoutineMock, tpsServiceRoutineMock);
    }

    @Test(expected = JMSException.class)
    public void exceptionDuringMessageSendingAreRethrown() throws Exception {
        when(messageQueueConsumerMock.sendMessage(anyString())).thenThrow(JMSException.class);

        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestServiceRoutineMock, tpsServiceRoutineMock);
    }
}
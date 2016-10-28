package no.nav.tps.vedlikehold.service.command.tps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;

import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConstants;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import no.nav.tps.vedlikehold.service.command.tps.utils.TpsRequestXmlCreator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.xml.XmlMapper;

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
    private TpsRequestEndringsmelding tpsRequestEndringsmelding;

    @Mock
    private TpsRequestXmlCreator tpsRequestXmlCreator;

    @Mock
    private MessageQueueConsumer messageQueueConsumerMock;

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
        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestServiceRoutineMock);

        verify(messageQueueServiceFactoryMock).createMessageQueueService(eq(ENVIRONMENT), eq(FasitConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS));
    }

    @Test
    public void aMessageIsSentToTps() throws Exception {
        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestServiceRoutineMock);
        verify(messageQueueConsumerMock).sendMessage(anyString());
    }

    @Test
    public void responseXmlIsProvided() throws Exception {
        String result = defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestServiceRoutineMock);
        assertThat(result, is(RESPONSE_XML));
    }

    @Test(expected = JMSException.class)
    public void exceptionDuringMessageQueueServiceCreationFailsGracefully() throws Exception {
        when(messageQueueServiceFactoryMock.createMessageQueueService(anyString(), anyString())).thenThrow(JMSException.class);

        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestServiceRoutineMock);
    }

    @Test(expected = JMSException.class)
    public void exceptionDuringMessageSendingAreRethrown() throws Exception {
        when(messageQueueConsumerMock.sendMessage(anyString())).thenThrow(JMSException.class);

        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestServiceRoutineMock);
    }
}
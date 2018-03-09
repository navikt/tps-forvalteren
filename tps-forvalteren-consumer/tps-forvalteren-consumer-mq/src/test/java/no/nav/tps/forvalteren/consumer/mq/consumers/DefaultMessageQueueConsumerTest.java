package no.nav.tps.forvalteren.consumer.mq.consumers;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.wmq.v6.jms.internal.JMSC;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Quite thorough testing, but there is a lot to keep in mind during the message exchange
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultMessageQueueConsumerTest {

    private static final String REQUEST_QUEUE_NAME  = "requestQueueName";

    private static final String REQUEST_MESSAGE  = "This is a test request";
    private static final String RESPONSE_MESSAGE = "This is a test response";

    private static final String MQ_DOES_NOT_ANSWER_ERROR = "MQ does not answer";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private MQConnectionFactory connectionFactoryMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private Session sessionMock;

    @Mock
    private MQQueue requestQueueMock;

    @Mock
    private MessageConsumer consumerMock;

    @Mock
    private MessageProducer producerMock;

    @Mock
    private TextMessage textMessageMock;

    @InjectMocks
    private DefaultMessageQueueConsumer messageQueueService = new DefaultMessageQueueConsumer(REQUEST_QUEUE_NAME, null);

    @Test
    public void altErBra(){

    }
//    @Before
//    public void setUp() throws JMSException {
//        when(connectionFactoryMock.createConnection(anyString(), anyString())).thenReturn(connectionMock);
//
//        when(connectionMock.createSession(anyBoolean(), anyInt())).thenReturn(sessionMock);
//
//        when(sessionMock.createQueue(eq(REQUEST_QUEUE_NAME))).thenReturn(requestQueueMock);
//        when(sessionMock.createProducer(eq(requestQueueMock))).thenReturn(producerMock);
//        when(sessionMock.createConsumer(any(), anyString())).thenReturn(consumerMock);
//
//        when(sessionMock.createTextMessage(anyString())).thenReturn(textMessageMock);
//        when(consumerMock.receive(anyLong())).thenReturn(textMessageMock);
//        when(textMessageMock.getText()).thenReturn(RESPONSE_MESSAGE);
//    }
//

//    @Test
//    public void createsNewConnection() throws JMSException {
//        messageQueueService.sendMessage(REQUEST_MESSAGE);
//
//        verify(connectionFactoryMock).createConnection(anyString(), anyString());
//    }
//
//    @Test
//    public void usesSessionToCreateQueues() throws JMSException {
//        messageQueueService.sendMessage(REQUEST_MESSAGE);
//
//        verify(sessionMock).createQueue(REQUEST_QUEUE_NAME);
//    }
//
//    @Test
//    public void usesConnectionToCreateNewSession() throws JMSException {
//        messageQueueService.sendMessage(REQUEST_MESSAGE);
//
//        InOrder inOrder = inOrder(connectionMock);
//
//        inOrder.verify(connectionMock).start();
//        inOrder.verify(connectionMock).createSession(eq(false), eq(Session.AUTO_ACKNOWLEDGE));
//    }
//
//    @Test
//    public void updatesTargetClientForTheRequestQueue() throws JMSException {
//        messageQueueService.sendMessage(REQUEST_MESSAGE);
//
//        verify(requestQueueMock).setTargetClient(eq(JMSC.MQJMS_CLIENT_NONJMS_MQ));
//    }
//
//    @Test
//    public void usesSessionToCreateProducersAndConsumers() throws JMSException {
//        messageQueueService.sendMessage(REQUEST_MESSAGE);
//
//        verify(sessionMock).createProducer(eq(requestQueueMock));
//    }
//
//    @Test
//    public void usesSessionToCreateTextMessage() throws JMSException {
//        messageQueueService.sendMessage(REQUEST_MESSAGE);
//
//        verify(sessionMock).createTextMessage(eq(REQUEST_MESSAGE));
//    }
//
//    @Test
//    public void connectionIsClosedAfterMessageIsReceived() throws JMSException {
//        messageQueueService.sendMessage(REQUEST_MESSAGE);
//
//        InOrder inOrder = inOrder(consumerMock, connectionMock);
//
//        inOrder.verify(consumerMock).receive(anyLong());
//        inOrder.verify(connectionMock).close();
//    }
//
//    @Test
//    public void correctMessageIsReturned() throws JMSException {
//        String responseMessage = messageQueueService.sendMessage(REQUEST_MESSAGE);
//
//        assertThat(responseMessage, is(RESPONSE_MESSAGE));
//    }
//
//    @Test
//    public void producerSendsTheMessage() throws JMSException {
//        messageQueueService.sendMessage(REQUEST_MESSAGE);
//
//        verify(producerMock).send(textMessageMock);
//    }
//
//    @Test
//    public void pingReturnsTrueWhenMqRespondsNormally() throws Exception {
//
//        boolean result = messageQueueService.ping();
//
//        assertThat(result, is(true));
//    }
//
//    @Test
//    public void pingThrowsExceptionWhenMqThrowsException() throws Exception {
//
//        RuntimeException thrownException = new RuntimeException(MQ_DOES_NOT_ANSWER_ERROR);
//
//        when(connectionMock.createSession(anyBoolean(), anyInt())).thenThrow(thrownException);
//
//        expectedException.expect(RuntimeException.class);
//        expectedException.expectMessage(MQ_DOES_NOT_ANSWER_ERROR);
//
//        boolean result = messageQueueService.ping();
//
//        assertThat(result, is(false));
//    }
}

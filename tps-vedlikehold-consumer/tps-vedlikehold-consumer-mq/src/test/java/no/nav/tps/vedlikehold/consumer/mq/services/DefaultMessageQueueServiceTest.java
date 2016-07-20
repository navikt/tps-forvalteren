package no.nav.tps.vedlikehold.consumer.mq.services;

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

import javax.jms.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Quite thorough testing, but there is a lot to keep in mind during the message exchange
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultMessageQueueServiceTest {

    private static final String REQUEST_QUEUE_NAME  = "requestQueueName";
    private static final String RESPONSE_QUEUE_NAME = "responseQueueName";

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
    private Queue responseQueueMock;

    @Mock
    private MessageConsumer consumerMock;

    @Mock
    private MessageProducer producerMock;

    @Mock
    private TextMessage textMessageMock;

    @InjectMocks
    private DefaultMessageQueueService messageQueueService = new DefaultMessageQueueService(REQUEST_QUEUE_NAME, RESPONSE_QUEUE_NAME, null);


    @Before
    public void setUp() throws JMSException {
        when(connectionFactoryMock.createConnection(anyString(), anyString())).thenReturn(connectionMock);

        when(connectionMock.createSession(anyBoolean(), anyInt())).thenReturn(sessionMock);

        when(sessionMock.createQueue(eq(REQUEST_QUEUE_NAME))).thenReturn(requestQueueMock);
        when(sessionMock.createQueue(eq(RESPONSE_QUEUE_NAME))).thenReturn(responseQueueMock);

        when(sessionMock.createProducer(eq(requestQueueMock))).thenReturn(producerMock);
        when(sessionMock.createConsumer(eq(responseQueueMock), anyString())).thenReturn(consumerMock);

        when(sessionMock.createTextMessage(anyString())).thenReturn(textMessageMock);
        when(consumerMock.receive(anyLong())).thenReturn(textMessageMock);
        when(textMessageMock.getText()).thenReturn(RESPONSE_MESSAGE);
    }


    @Test
    public void createsNewConnection() throws JMSException {
        messageQueueService.sendMessage(REQUEST_MESSAGE);

        verify(connectionFactoryMock).createConnection(anyString(), anyString());
    }

    @Test
    public void usesSessionToCreateQueues() throws JMSException {
        messageQueueService.sendMessage(REQUEST_MESSAGE);

        verify(sessionMock).createQueue(REQUEST_QUEUE_NAME);
        verify(sessionMock).createQueue(RESPONSE_QUEUE_NAME);
    }

    @Test
    public void usesConnectionToCreateNewSession() throws JMSException {
        messageQueueService.sendMessage(REQUEST_MESSAGE);

        InOrder inOrder = inOrder(connectionMock);

        inOrder.verify(connectionMock).start();
        inOrder.verify(connectionMock).createSession(eq(false), eq(Session.AUTO_ACKNOWLEDGE));
    }

    @Test
    public void updatesTargetClientForTheRequestQueue() throws JMSException {
        messageQueueService.sendMessage(REQUEST_MESSAGE);

        verify(requestQueueMock).setTargetClient(eq(JMSC.MQJMS_CLIENT_NONJMS_MQ));
    }

    @Test
    public void usesSessionToCreateProducersAndConsumers() throws JMSException {
        messageQueueService.sendMessage(REQUEST_MESSAGE);

        verify(sessionMock).createProducer(eq(requestQueueMock));
        verify(sessionMock).createConsumer(eq(responseQueueMock), anyString());
    }

    @Test
    public void usesSessionToCreateTextMessage() throws JMSException {
        messageQueueService.sendMessage(REQUEST_MESSAGE);

        verify(sessionMock).createTextMessage(eq(REQUEST_MESSAGE));
    }

    @Test
    public void connectionIsClosedAfterMessageIsReceived() throws JMSException {
        messageQueueService.sendMessage(REQUEST_MESSAGE);

        InOrder inOrder = inOrder(consumerMock, connectionMock);

        inOrder.verify(consumerMock).receive(anyLong());
        inOrder.verify(connectionMock).close();
    }

    @Test
    public void correctMessageIsReturned() throws JMSException {
        String responseMessage = messageQueueService.sendMessage(REQUEST_MESSAGE);

        assertThat(responseMessage, is(RESPONSE_MESSAGE));
    }

    @Test
    public void producerSendsTheMessage() throws JMSException {
        messageQueueService.sendMessage(REQUEST_MESSAGE);

        verify(producerMock).send(textMessageMock);
    }

    @Test
    public void responseQueueNameIsDefined() throws JMSException {
        messageQueueService.sendMessage(REQUEST_MESSAGE);

        verify(textMessageMock).setJMSReplyTo(eq(responseQueueMock));
    }

    @Test
    public void pingReturnsTrueWhenMqRespondsNormally() throws Exception {

        boolean result = messageQueueService.ping();

        assertThat(result, is(true));
    }

    @Test
    public void pingThrowsExceptionWhenMqThrowsException() throws Exception {

        RuntimeException thrownException = new RuntimeException(MQ_DOES_NOT_ANSWER_ERROR);

        when(connectionMock.createSession(anyBoolean(), anyInt())).thenThrow(thrownException);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(MQ_DOES_NOT_ANSWER_ERROR);

        boolean result = messageQueueService.ping();

        assertThat(result, is(false));
    }
}

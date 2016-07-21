package no.nav.tps.vedlikehold.consumer.mq.consumers;

import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.wmq.v6.jms.internal.JMSC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class DefaultMessageQueueConsumer implements MessageQueueConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMessageQueueConsumer.class);

    private static final String MESSAGE_QUEUE_USERNAME  = "srvappserver";
    private static final String MESSAGE_QUEUE_PASSWORD  = "";
    private static final long DEFAULT_TIMEOUT           = 5000;

    private static final String PING_MESSAGE            = "ping message";

    private String requestQueueName;
    private String responseQueueName;
    private ConnectionFactory connectionFactory;

    public DefaultMessageQueueConsumer(String requestQueueName, String responseQueueName, ConnectionFactory connectionFactory) {
        this.requestQueueName  = requestQueueName;
        this.responseQueueName = responseQueueName;
        this.connectionFactory = connectionFactory;
    }

    public String sendMessage(String requestMessageContent) throws JMSException {
        return sendMessage(requestMessageContent, DEFAULT_TIMEOUT);
    }

    public String sendMessage(String requestMessageContent, long timeout) throws JMSException {
        /* Initiate session */
        LOGGER.debug("Creating MQ connection");
        Connection connection = connectionFactory.createConnection(MESSAGE_QUEUE_USERNAME, MESSAGE_QUEUE_PASSWORD);

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        /* Prepare destinations */
        LOGGER.debug("Creating queue: {}", requestQueueName);
        Destination requestDestination  = session.createQueue( requestQueueName );

        LOGGER.debug("Creating queue: {}", responseQueueName);
        Destination responseDestination = session.createQueue(responseQueueName);

        ((MQQueue) requestDestination).setTargetClient(JMSC.MQJMS_CLIENT_NONJMS_MQ);            //TODO: This method should be provider independent

        /* Prepare request message */
        TextMessage requestMessage = session.createTextMessage(requestMessageContent);
        MessageProducer producer   = session.createProducer(requestDestination);

        requestMessage.setJMSReplyTo(responseDestination);

        LOGGER.debug("Sending message: {}", requestMessage);
        producer.send(requestMessage);

        /* Wait for response */
        String attributes = String.format("JMSCorrelationID='%s'", requestMessage.getJMSMessageID());

        MessageConsumer consumer = session.createConsumer(responseDestination, attributes);

        TextMessage responseMessage = (TextMessage) consumer.receive(timeout);
        LOGGER.debug("Received message: {}", responseMessage);

        /* Close the queues, the session, and the connection */
        connection.close();

        return responseMessage.getText();
    }

    public boolean ping() throws JMSException {
        this.sendMessage(PING_MESSAGE);
        return true;
    }
}

package no.nav.tps.forvalteren.consumer.mq.consumers;

import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.v6.jms.internal.JMSC;
import no.nav.tps.forvalteren.consumer.mq.config.MessageQueueConsumerConstants;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMessageQueueConsumer implements MessageQueueConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMessageQueueConsumer.class);

    private static final long DEFAULT_TIMEOUT = 5000;
    private static final long ZERO_TIMEOUT = 0;

    private static final String PING_MESSAGE = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData xmlns=\"http://www.rtv.no/NamespaceTPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.rtv.no/NamespaceTPS H:\\SYSTEM~1\\SYSTEM~4\\FS03TP~1\\TPSDAT~1.XSD\"><tpsServiceRutine><serviceRutinenavn>FS03-OTILGANG-TILSRTPS-O</serviceRutinenavn></tpsServiceRutine></tpsPersonData>";

    private String requestQueueName;
    private ConnectionFactory connectionFactory;

    public DefaultMessageQueueConsumer(String requestQueueName, ConnectionFactory connectionFactory) {
        this.requestQueueName = requestQueueName;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void sendMessageAsync(String requestMessageContent) throws JMSException{
        sendMessage(requestMessageContent,ZERO_TIMEOUT);
    }

    @Override
    public String sendMessage(String requestMessageContent) throws JMSException {
        return sendMessage(requestMessageContent, DEFAULT_TIMEOUT);
    }

    @Override
    public String sendMessage(String requestMessageContent, long timeout) throws JMSException {

        LOGGER.info("---------------------------");
        LOGGER.info("username: {}", MessageQueueConsumerConstants.USERNAME);
        LOGGER.info("password: {}", MessageQueueConsumerConstants.PASSWORD);
        LOGGER.info("MQ queue name: {}", requestQueueName);
        LOGGER.info("MQ manager name: {}", ((MQQueueConnectionFactory) connectionFactory).getQueueManager());
        LOGGER.info("MQ channel name: {}", ((MQQueueConnectionFactory) connectionFactory).getChannel());

        Connection connection = connectionFactory.createConnection(MessageQueueConsumerConstants.USERNAME, MessageQueueConsumerConstants.PASSWORD);
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        /* Prepare destinations */
        Destination requestDestination = session.createQueue(requestQueueName);

        Destination responseDestination = createTemporaryQueueFor(session);

        if (requestDestination instanceof MQQueue) {
            ((MQQueue) requestDestination).setTargetClient(JMSC.MQJMS_CLIENT_NONJMS_MQ);            //TODO: This method should be provider independent
        }

        /* Prepare request message */
        TextMessage requestMessage = session.createTextMessage(requestMessageContent);
        MessageProducer producer = session.createProducer(requestDestination);

        requestMessage.setJMSReplyTo(responseDestination);

        producer.send(requestMessage);

        TextMessage responseMessage = null;
        if(timeout > 0){
            /* Wait for response */
            String attributes = String.format("JMSCorrelationID='%s'", requestMessage.getJMSMessageID());

            MessageConsumer consumer = session.createConsumer(responseDestination, attributes);

            responseMessage = (TextMessage) consumer.receive(timeout);
        }

        /* Close the queues, the session, and the connection */
        connection.close();

        String response = responseMessage != null ? responseMessage.getText() : "";
        LOGGER.info("Response: {}", response);
        LOGGER.info("---------------------------");

        return responseMessage != null ? responseMessage.getText() : "";

    }

    public Destination createTemporaryQueueFor(Session session) throws JMSException {
        return session.createTemporaryQueue();
    }

    @Override
    public boolean ping() throws JMSException {
        this.sendMessage(PING_MESSAGE);
        return true;
    }
}

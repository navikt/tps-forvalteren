package no.nav.tps.vedlikehold.consumer.mq;

import org.springframework.jms.core.SessionCallback;

import javax.jms.*;
import java.util.Random;

/**
 * Created by Øyvind Grimnes, Visma Consulting AS on 06.07.2016.
 */
public class MessageQueueSessionCallback implements SessionCallback<TextMessage> {

    private String messageContent;
    private String requestQueueName;
    private String responseQueueName;
    private long timeout;

    public MessageQueueSessionCallback(String messageContent, String requestQueueName, String responseQueueName, long timeout) {
        this.messageContent = messageContent;
        this.requestQueueName = requestQueueName;
        this.responseQueueName = responseQueueName;
        this.timeout = timeout;
    }

    @Override
    public TextMessage doInJms(Session session) throws JMSException {
        Queue requestQueue = session.createQueue(requestQueueName);
        Queue responseQueue = session.createQueue(responseQueueName);

        /* Send message */
        MessageProducer producer = session.createProducer(requestQueue);

        final TextMessage message = session.createTextMessage(messageContent);
        message.setJMSReplyTo(responseQueue);

        producer.send(message);

        message.setJMSCorrelationID( Long.toHexString(new Random(System.currentTimeMillis()).nextLong()) );

        /* Wait for response */
        String attributes = "JMSCorrelationID='" + message.getJMSCorrelationID() + "'";
        MessageConsumer consumer = session.createConsumer(responseQueue, attributes);

        TextMessage response = (TextMessage) consumer.receive(timeout);

        producer.close();

        return response;
    }
}

package no.nav.tps.forvalteren.consumer.mq.consumers;

import javax.jms.JMSException;


public interface MessageQueueConsumer {
    String sendMessage(String requestMessageContent) throws JMSException;
    String sendMessage(String requestMessageContent, long timeout) throws JMSException;
    void sendMessageAsync(String requestMessageContent) throws JMSException;

    boolean ping() throws JMSException;
}

package no.nav.tps.vedlikehold.consumer.mq.consumers;

import javax.jms.JMSException;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface MessageQueueConsumer {
    String sendMessage(String requestMessageContent) throws JMSException;
    String sendMessage(String requestMessageContent, long timeout) throws JMSException;
    void setRequestQueue(String requestQueue);

    boolean ping() throws JMSException;
}

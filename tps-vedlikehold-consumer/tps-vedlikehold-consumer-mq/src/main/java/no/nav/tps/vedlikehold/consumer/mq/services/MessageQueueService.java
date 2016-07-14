package no.nav.tps.vedlikehold.consumer.mq.services;

import javax.jms.JMSException;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface MessageQueueService {
    String sendMessage(String requestMessageContent) throws JMSException;
    String sendMessage(String requestMessageContent, long timeout) throws JMSException;
}

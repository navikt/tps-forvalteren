package no.nav.tps.vedlikehold.consumer.mq.factories;

import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;

import javax.jms.JMSException;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@FunctionalInterface
public interface MessageQueueServiceFactory {
    MessageQueueConsumer createMessageQueueService(String environment, String requestQueueAlias) throws JMSException;
}

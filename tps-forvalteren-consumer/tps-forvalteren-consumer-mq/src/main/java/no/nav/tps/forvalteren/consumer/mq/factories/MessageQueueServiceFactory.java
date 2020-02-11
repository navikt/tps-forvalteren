package no.nav.tps.forvalteren.consumer.mq.factories;

import javax.jms.JMSException;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;

@FunctionalInterface
public interface MessageQueueServiceFactory {
    MessageQueueConsumer createMessageQueueConsumer(String environment, String requestQueueAlias, boolean isQueueName) throws JMSException;
}

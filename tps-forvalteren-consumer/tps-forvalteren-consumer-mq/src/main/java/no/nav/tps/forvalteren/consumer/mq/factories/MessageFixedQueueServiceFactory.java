package no.nav.tps.forvalteren.consumer.mq.factories;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;

import javax.jms.JMSException;

@FunctionalInterface
public interface MessageFixedQueueServiceFactory {
    MessageQueueConsumer createMessageQueueConsumerWithFixedQueueName(String environment, String fixedQueueName) throws JMSException;
}

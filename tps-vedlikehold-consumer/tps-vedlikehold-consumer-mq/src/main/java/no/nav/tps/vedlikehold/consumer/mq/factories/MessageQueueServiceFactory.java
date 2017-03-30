package no.nav.tps.vedlikehold.consumer.mq.factories;

import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;

import javax.jms.JMSException;


@FunctionalInterface
public interface MessageQueueServiceFactory {
    MessageQueueConsumer createMessageQueueService(String environment, String requestQueueAlias) throws JMSException;
}

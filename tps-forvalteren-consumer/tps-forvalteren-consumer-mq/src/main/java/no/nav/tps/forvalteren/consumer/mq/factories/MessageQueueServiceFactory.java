package no.nav.tps.forvalteren.consumer.mq.factories;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;

import javax.jms.JMSException;


public interface MessageQueueServiceFactory {
    MessageQueueConsumer createMessageQueueConsumer(String environment, String requestQueueAlias) throws JMSException;
}

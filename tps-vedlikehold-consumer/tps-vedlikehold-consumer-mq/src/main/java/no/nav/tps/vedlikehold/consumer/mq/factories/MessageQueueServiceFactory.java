package no.nav.tps.vedlikehold.consumer.mq.factories;

import javax.jms.JMSException;
import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@FunctionalInterface
public interface MessageQueueServiceFactory {
    MessageQueueConsumer createMessageQueueService(String environment) throws JMSException;
}

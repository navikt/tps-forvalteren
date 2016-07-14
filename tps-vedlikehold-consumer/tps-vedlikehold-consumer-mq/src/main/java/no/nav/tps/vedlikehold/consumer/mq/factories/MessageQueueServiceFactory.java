package no.nav.tps.vedlikehold.consumer.mq.factories;

import no.nav.tps.vedlikehold.consumer.mq.services.DefaultMessageQueueService;

import javax.jms.JMSException;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface MessageQueueServiceFactory {
    DefaultMessageQueueService createMessageQueueService(String environment) throws JMSException;
}

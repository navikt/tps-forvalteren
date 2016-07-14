package no.nav.tps.vedlikehold.consumer.mq.factories;

import no.nav.tps.vedlikehold.consumer.mq.strategies.ConnectionFactoryStrategy;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface ConnectionFactoryFactory {
    ConnectionFactory createConnectionFactory(ConnectionFactoryStrategy strategy) throws JMSException;
}

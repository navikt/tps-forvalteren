package no.nav.tps.vedlikehold.consumer.mq.factories;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import no.nav.tps.vedlikehold.consumer.mq.factories.strategies.ConnectionFactoryStrategy;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@FunctionalInterface
public interface ConnectionFactoryFactory {
    ConnectionFactory createConnectionFactory(ConnectionFactoryStrategy strategy) throws JMSException;
}

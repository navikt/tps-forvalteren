package no.nav.tps.forvalteren.consumer.mq.factories;

import no.nav.tps.forvalteren.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;


@FunctionalInterface
public interface ConnectionFactoryFactory {
    ConnectionFactory createConnectionFactory(ConnectionFactoryFactoryStrategy strategy) throws JMSException;
}

package no.nav.tps.forvalteren.consumer.mq.factories;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import no.nav.tps.forvalteren.common.java.tpsapi.QueueManager;

@FunctionalInterface
public interface ConnectionFactoryFactory {
    ConnectionFactory createConnectionFactory(QueueManager queueManager) throws JMSException;
}

package no.nav.tps.vedlikehold.consumer.mq.factories.strategies;

/**
 * Provides information needed when creating a queue connection factory
 *
 */
public interface ConnectionFactoryFactoryStrategy {
    String getName();
    String getHostName();
    String getChannelName();
    Integer getPort();
    Integer getTransportType();
}

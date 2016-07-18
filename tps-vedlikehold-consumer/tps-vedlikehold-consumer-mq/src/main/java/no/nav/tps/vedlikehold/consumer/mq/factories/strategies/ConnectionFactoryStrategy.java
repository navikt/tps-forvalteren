package no.nav.tps.vedlikehold.consumer.mq.factories.strategies;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface ConnectionFactoryStrategy {
    String getName();
    String getHostName();
    String getChannelName();
    Integer getPort();
    Integer getTransportType();
}

package no.nav.tps.vedlikehold.consumer.mq.factories.strategies;

import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;

import static no.nav.tps.vedlikehold.consumer.mq.config.MessageQueueConsumerConstants.CHANNEL_POSTFIX;

/**
 * Provides information needed when creating a queue connection factory
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class QueueManagerConnectionFactoryFactoryStrategy implements ConnectionFactoryFactoryStrategy {

    private QueueManager queueManager;
    private String environment;

    public QueueManagerConnectionFactoryFactoryStrategy(QueueManager queueManager, String environment) {
        this.queueManager = queueManager;
        this.environment = environment;
    }

    @Override
    public String getName() {
        return queueManager.getName();
    }

    @Override
    public String getHostName() {
        return queueManager.getHostname();
    }

    @Override
    public Integer getPort() {
        return Integer.parseInt( queueManager.getPort() );
    }

    @Override
    public Integer getTransportType() {
        return 1;
    }

    @Override
    public String getChannelName() {
        return environment.toUpperCase() + CHANNEL_POSTFIX;
    }
}

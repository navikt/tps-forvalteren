package no.nav.tps.vedlikehold.consumer.mq.factories.strategies;

import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class QueueManagerConnectionFactoryStrategy implements ConnectionFactoryStrategy {

    private QueueManager queueManager;

    public QueueManagerConnectionFactoryStrategy(QueueManager queueManager) {
        this.queueManager = queueManager;
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
        return "T4_SAKOGBEHANDLING";                            //TODO: Make dynamic [PKJAG-2685]
    }
}

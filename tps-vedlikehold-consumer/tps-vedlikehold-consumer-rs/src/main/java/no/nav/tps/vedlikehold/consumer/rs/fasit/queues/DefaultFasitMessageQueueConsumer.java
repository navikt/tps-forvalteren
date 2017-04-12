package no.nav.tps.vedlikehold.consumer.rs.fasit.queues;

import no.nav.tps.vedlikehold.consumer.rs.fasit.FasitClient;
import no.nav.tps.vedlikehold.domain.ws.fasit.Queue;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Convenience class for exposing MQ resources.
 * Connects to the application's Fasit resources and exposes its request and response queues.
 * It connects to tpsws Fasit resources
 */

public class DefaultFasitMessageQueueConsumer implements FasitMessageQueueConsumer {

    @Autowired
    private FasitClient fasitClient;

    private String applicationName;

    private String requestQueueAlias;
    private String queueManagerAlias;

    public DefaultFasitMessageQueueConsumer(String application, String requestQueueAlias, String queueManagerAlias) {
        this.applicationName = application;
        this.requestQueueAlias = requestQueueAlias;
        this.queueManagerAlias = queueManagerAlias;
    }

    @Override
    public void setRequestQueueAlias(String requestQueueAlias){
        this.requestQueueAlias = requestQueueAlias;
    }

    @Override
    public Queue getRequestQueue(String environment) {
        return getQueue(requestQueueAlias, environment);
    }

    @Override
    public Queue getQueue(String alias, String environment) {
        return getApplication(environment)
                .getQueue(alias);
    }

    @Override
    public QueueManager getQueueManager(String environment) {
        return getApplication(environment)
                .getQueueManager(queueManagerAlias);
    }

    @Override
    public QueueManager getQueueManager(String alias, String environment) {
        return getApplication(environment)
                .getQueueManager(alias);
    }

    private FasitClient.Application getApplication(String environment) {
        return fasitClient.getApplication(applicationName, environment);
    }
}

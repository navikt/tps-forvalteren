package no.nav.tps.forvalteren.consumer.rs.fasit.queues;

import no.nav.tps.forvalteren.consumer.rs.fasit.FasitClient;
import no.nav.tps.forvalteren.domain.ws.fasit.Queue;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Convenience class for exposing MQ resources.
 * Connects to the application's Fasit resources and exposes its request and response queues.
 * It connects to tpsws Fasit resources
 */

@Component
public class DefaultFasitMessageQueueConsumer implements FasitMessageQueueConsumer {

    @Autowired
    private FasitClient fasitClient;

    @Override
    public Queue getRequestQueue(String requestQueueAlias, String environment) {
        return fasitClient.getQueue(requestQueueAlias, environment);
    }

    @Override
    public QueueManager getQueueManager() {
        return fasitClient.getQueueManager();
    }
}

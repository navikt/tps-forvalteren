package no.nav.tps.forvalteren.consumer.rs.fasit.queues;

import no.nav.tps.forvalteren.domain.ws.fasit.Queue;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;

public interface FasitMessageQueueConsumer  {
    Queue getRequestQueue(String requestQueueAlias, String environment);
    QueueManager getQueueManager(String environment);
}

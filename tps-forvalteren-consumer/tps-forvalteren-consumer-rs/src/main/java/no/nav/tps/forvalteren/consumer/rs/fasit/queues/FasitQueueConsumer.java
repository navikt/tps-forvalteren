package no.nav.tps.forvalteren.consumer.rs.fasit.queues;

import no.nav.tps.forvalteren.domain.ws.fasit.Queue;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;


public interface FasitQueueConsumer {
    Queue getQueue(String alias, String environment);
    QueueManager getQueueManager(String alias, String environment);
}


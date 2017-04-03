package no.nav.tps.vedlikehold.consumer.rs.fasit.queues;

import no.nav.tps.vedlikehold.domain.ws.fasit.Queue;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;


public interface FasitQueueConsumer {
    Queue getQueue(String alias, String environment);
    QueueManager getQueueManager(String alias, String environment);
}


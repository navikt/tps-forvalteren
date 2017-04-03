package no.nav.tps.vedlikehold.consumer.rs.fasit.queues;

import no.nav.tps.vedlikehold.domain.ws.fasit.Queue;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;


public interface FasitMessageQueueConsumer extends FasitQueueConsumer {
    Queue getRequestQueue(String environment);
    Queue getResponseQueue(String environment);
    QueueManager getQueueManager(String environment);
    void setRequestQueueAlias(String requestQueueAlias);
}

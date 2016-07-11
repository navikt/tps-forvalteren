package no.nav.tps.vedlikehold.consumer.ws.fasit.queue;


import no.nav.tps.vedlikehold.domain.ws.fasit.Queue;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface FasitQueueConsumer {
    Queue getQueue(String alias, String environment);
    QueueManager getQueueManager(String alias, String environment);
}


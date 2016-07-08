package no.nav.tps.vedlikehold.consumer.ws.fasit.queue;


import no.nav.tps.vedlikehold.consumer.ws.fasit.FasitClient;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface FasitQueueConsumer {
    FasitClient.Queue getQueue(String alias, String environment);
    FasitClient.QueueManager getQueueManager(String alias, String environment);
}


package no.nav.tps.vedlikehold.consumer.ws.fasit.queue;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
interface FasitQueueConsumer {
    String getQueue(String alias, String environment);
}


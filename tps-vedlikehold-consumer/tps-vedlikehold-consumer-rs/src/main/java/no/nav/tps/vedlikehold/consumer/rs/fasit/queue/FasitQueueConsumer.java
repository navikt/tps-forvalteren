package no.nav.tps.vedlikehold.consumer.rs.fasit.queue;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
interface FasitQueueConsumer {
    String getQueue(String alias, String environment);
}


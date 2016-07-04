package no.nav.tps.vedlikehold.consumer.ws.fasit.queue;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface FasitQueueConsumer {
    String getQueue(String alias, String environment);
}


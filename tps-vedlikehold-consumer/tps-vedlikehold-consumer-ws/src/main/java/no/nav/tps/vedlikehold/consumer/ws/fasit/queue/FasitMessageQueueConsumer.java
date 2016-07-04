package no.nav.tps.vedlikehold.consumer.ws.fasit.queue;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface FasitMessageQueueConsumer {
    String getRequestQueue(String environment);
    String getResponseQueue(String environment);
}

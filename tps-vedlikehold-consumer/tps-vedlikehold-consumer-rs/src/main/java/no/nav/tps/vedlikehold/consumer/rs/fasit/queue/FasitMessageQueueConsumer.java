package no.nav.tps.vedlikehold.consumer.rs.fasit.queue;

/**
 * Created by Øyvind Grimnes, Visma Consulting AS on 04.07.2016.
 */
public interface FasitMessageQueueConsumer {
    String getRequestQueue(String environment);
    String getResponseQueue(String environment);
}

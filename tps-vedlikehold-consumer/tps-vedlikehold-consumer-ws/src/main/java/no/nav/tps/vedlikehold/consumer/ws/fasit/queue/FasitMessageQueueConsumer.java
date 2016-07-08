package no.nav.tps.vedlikehold.consumer.ws.fasit.queue;

import no.nav.tps.vedlikehold.consumer.ws.fasit.FasitClient;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface FasitMessageQueueConsumer extends FasitQueueConsumer {
    FasitClient.Queue getRequestQueue(String environment);
    FasitClient.Queue getResponseQueue(String environment);
}

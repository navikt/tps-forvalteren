package no.nav.tps.vedlikehold.consumer.ws.fasit.queue;

import no.nav.tps.vedlikehold.domain.ws.fasit.Queue;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface FasitMessageQueueConsumer extends FasitQueueConsumer {
    Queue getRequestQueue(String environment);
    Queue getResponseQueue(String environment);
}

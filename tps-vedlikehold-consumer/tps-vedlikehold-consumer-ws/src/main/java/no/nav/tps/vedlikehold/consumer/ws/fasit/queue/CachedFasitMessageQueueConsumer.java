package no.nav.tps.vedlikehold.consumer.ws.fasit.queue;

/**
 * Convenience class for exposing MQ resources.
 * Connects to the application's Fasit resources and exposes its request and response queues.
 * Retrieved queues are cached.
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class CachedFasitMessageQueueConsumer extends CachedFasitQueueConsumer implements FasitMessageQueueConsumer {

    private String requestQueueAlias;
    private String responseQueueAlias;

    public CachedFasitMessageQueueConsumer(String application, String requestQueueAlias, String responseQueueAlias) {
        super(application);

        this.requestQueueAlias = requestQueueAlias;
        this.responseQueueAlias = responseQueueAlias;
    }

    @Override
    public String getRequestQueue(String environment) {
        return getQueue(requestQueueAlias, environment);
    }

    @Override
    public String getResponseQueue(String environment) {
        return getQueue(responseQueueAlias, environment);
    }
}

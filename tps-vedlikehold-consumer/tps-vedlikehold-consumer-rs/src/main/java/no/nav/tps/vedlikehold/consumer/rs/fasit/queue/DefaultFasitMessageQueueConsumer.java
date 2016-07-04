package no.nav.tps.vedlikehold.consumer.rs.fasit.queue;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class DefaultFasitMessageQueueConsumer extends DefaultFasitQueueConsumer implements FasitMessageQueueConsumer {

    private String requestQueueAlias;
    private String responseQueueAlias;

    public DefaultFasitMessageQueueConsumer(String application, String requestQueueAlias, String responseQueueAlias) {
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

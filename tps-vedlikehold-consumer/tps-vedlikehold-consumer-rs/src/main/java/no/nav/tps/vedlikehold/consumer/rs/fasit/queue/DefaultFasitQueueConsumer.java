package no.nav.tps.vedlikehold.consumer.rs.fasit.queue;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import no.nav.brevogarkiv.common.fasit.FasitClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;


/**
 * Connects to the application's Fasit resources and exposes its queues.
 * Retrieved queues are cached.
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class DefaultFasitQueueConsumer implements FasitQueueConsumer {

    @Autowired
    private FasitClient fasitClient;

    private String applicationName;
    private Cache<String, Object> cache;

    public DefaultFasitQueueConsumer( String applicationName ) {
        this.applicationName = applicationName;

        this.cache = CacheBuilder.newBuilder()
                                 .maximumSize(100)
                                 .expireAfterWrite(10, TimeUnit.MINUTES)
                                 .build();
    }

    /* Queues */

    public String getQueue(String alias, String environment) {
        String queue = (String) getResourceFromCache(alias, environment);

        /* The queue was cached */
        if (queue != null) {
            return queue;
        }

        FasitClient.Application application = getApplication(environment);

        /* The application does not exist in the environment */
        if (application == null) {
            return null;
        }

        /* Retrieve queue from Fasit and cache the result */
        queue = application.getQueue(alias);
        addResourceToCache(queue, alias, environment);

        return queue;
    }

    private FasitClient.Application getApplication(String environment) {
        return fasitClient.getApplication(environment, applicationName);
    }

    /* Cache */

    private Object getResourceFromCache(String alias, String environment) {
        String identifier = getIdentifier(alias, environment);
        return cache.getIfPresent(identifier);
    }

    private void addResourceToCache(Object resource, String alias, String environment) {
        String identifier = getIdentifier(alias, environment);
        cache.put(identifier, resource);
    }

    private String getIdentifier(String alias, String environment) {
        return environment + "." + alias;
    }
}

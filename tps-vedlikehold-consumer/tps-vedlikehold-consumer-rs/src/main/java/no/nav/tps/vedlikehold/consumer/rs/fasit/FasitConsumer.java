package no.nav.tps.vedlikehold.consumer.rs.fasit;

import com.google.common.cache.Cache;
import no.nav.brevogarkiv.common.fasit.FasitClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Connects to the TPSWS application's Fasit resources
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Service
public class FasitConsumer {

    @Value("${fasit.tpsws.application}")
    private String application;

    @Value("${fasit.tpsws.message.queue.request}")
    private String messageQueueRequest;

    @Value("${fasit.tpsws.message.queue.response}")
    private String messageQueueResponse;

    @Autowired
    private FasitClient fasitClient;

    @Autowired
    private Cache<String, Object> cache;

    @PostConstruct
    public void asdasd() {
        System.out.println(getRequestQueue("u5"));
        System.out.println(getRequestQueue("u5"));
        System.out.println(getRequestQueue("u5"));
        System.out.println(getRequestQueue("u5"));
    }

    /* Queues */

    public String getRequestQueue(String environment) {
        return getQueue(messageQueueRequest, environment);
    }

    public String getResponseQueue(String environment) {
        return getQueue(messageQueueResponse, environment);
    }

    private String getQueue(String alias, String environment) {
        String queue = (String) getResourceFromCache(alias, environment);

        if (queue == null) {
            queue = getApplication(environment).getQueue(alias);
            addResourceToCache(queue, alias, environment);
        }

        return queue;
    }

    /* Application */

    private FasitClient.Application getApplication(String environment) {
        return fasitClient.getApplication(environment, application);
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

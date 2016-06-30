package no.nav.tps.vedlikehold.consumer.rs.fasit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import no.nav.brevogarkiv.common.fasit.FasitClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

/**
 * Connects to the TPSWS application's Fasit resources
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Service
public class FasitConsumer {
    //TODO: Could be configured in fasit/applicationproperties?
    private static final String APPLICATION_NAME        = "tpsws";
    private static final String MESSAGE_QUEUE_REQUEST   = "tps.endrings.melding";
    private static final String MESSAGE_QUEUE_RESPONSE  = "tps.endrings.melding.svar";

    @Autowired
    private FasitClient fasitClient;

    @Autowired
    private Cache<String, String> cache;

    @PostConstruct
    public void asdasd() {
        System.out.println(getRequestQueue("u5"));
        System.out.println(getRequestQueue("u5"));
        System.out.println(getRequestQueue("u5"));
        System.out.println(getRequestQueue("u5"));
    }

    public FasitClient.Application getApplication(String environment) {
        return fasitClient.getApplication(environment, APPLICATION_NAME);
    }

    public String getRequestQueue(String environment) {
        return getQueue(MESSAGE_QUEUE_REQUEST, environment);
    }

    public String getResponseQueue(String environment) {
        return getQueue(MESSAGE_QUEUE_RESPONSE, environment);
    }

    private String getQueue(String resource, String environment) {
        String identifier = identifier(resource, environment);
        String queue    = cache.getIfPresent(identifier);

        if (queue == null) {
            queue = getApplication(environment).getQueue(resource);
            cache.put(identifier, queue);
        }

        return queue;
    }

    private String identifier(String resource, String environment) {
        return environment + "." + resource;
    }

}

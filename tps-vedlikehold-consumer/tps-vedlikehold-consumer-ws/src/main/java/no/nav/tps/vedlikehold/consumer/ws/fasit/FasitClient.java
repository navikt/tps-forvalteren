package no.nav.tps.vedlikehold.consumer.ws.fasit;

import java.util.concurrent.TimeUnit;

import no.nav.aura.envconfig.client.DomainDO;
import no.nav.aura.envconfig.client.FasitRestClient;
import no.nav.aura.envconfig.client.ResourceTypeDO;
import no.nav.aura.envconfig.client.rest.ResourceElement;
import no.nav.tps.vedlikehold.domain.ws.fasit.Queue;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class FasitClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FasitClient.class);

    private static final long CACHE_MAX_SIZE        = 100;
    private static final long CACHE_MINUTES_TO_LIVE = 30;
    private static final String PING_ENVIRONMENT = "t4";
    private static final String PING_ALIAS = "mqGateway";
    private static final ResourceTypeDO PING_TYPE = ResourceTypeDO.QueueManager;
    private static final String PING_APPLICATION_NAME = "tpsws";

    private FasitRestClient restClient;

    private Cache<String, ResourceElement> cache;

    public FasitClient(String baseUrl, String username, String password) {
        this.restClient = new FasitRestClient(baseUrl, username, password);

        this.cache = CacheBuilder.newBuilder()
                                 .maximumSize(CACHE_MAX_SIZE)
                                 .expireAfterWrite(CACHE_MINUTES_TO_LIVE, TimeUnit.MINUTES)
                                 .build();

        this.restClient.useCache(false); // The rest client's cache is never updated
    }


    public ResourceElement findResource(String alias, String applicationName, String environment, ResourceTypeDO type) {
        ResourceElement resource = getFromCache(alias, applicationName, environment, type);

        if (resource != null) {
            return resource;
        }

        DomainDO domain = FasitUtilities.domainFor(environment);

        resource = this.restClient.getResource(environment, alias, type, domain, applicationName);

        addToCache(resource, alias, applicationName, environment, type);

        return resource;
    }

    /* Cache */

    private ResourceElement getFromCache(String alias, String applicationName, String environment, ResourceTypeDO type) {
        String identifier = getIdentifier(alias, applicationName, environment, type);
        return cache.getIfPresent(identifier);
    }

    private void addToCache(ResourceElement resource, String alias, String applicationName, String environment, ResourceTypeDO type) {
        String identifier = getIdentifier(alias, applicationName, environment, type);
        cache.put(identifier, resource);
    }

    private String getIdentifier(String alias, String applicationName, String environment, ResourceTypeDO type) {
        return String.format("%s.%s.%s.%s", environment, applicationName, alias,  type.name());
    }

    public boolean ping() {
        try {
            DomainDO domain = FasitUtilities.domainFor(PING_ENVIRONMENT);

            this.restClient.getResource(PING_ENVIRONMENT, PING_ALIAS, PING_TYPE, domain, PING_APPLICATION_NAME);
        } catch (RuntimeException exception) {
            LOGGER.warn("Pinging Fasit failed with exception: {}", exception.toString());
            throw exception;
        }
        return true;
    }

    /* Application */

    public Application getApplication(String name, String environment) {
        return new Application(name, environment);
    }

    /**
     *  A convenience type used to keep track of the environment and application of interest
     *  Helps reduce the size of method calls, and reduces the need for local variables
     *
     *  Used to access the FasitClient's internal methods
     */
    public class Application {
        String environment;
        String name;

        protected Application(String name, String environment) {
            this.name        = name;
            this.environment = environment;
        }

        public QueueManager getQueueManager(String alias) {
            ResourceElement resource = FasitClient.this.findResource(alias, name, environment, ResourceTypeDO.QueueManager);

            String managerName = resource.getPropertyString("name");
            String hostname    = resource.getPropertyString("hostname");
            String port        = resource.getPropertyString("port");

            return new QueueManager(managerName, hostname, port);
        }

        public Queue getQueue(String alias) {
            ResourceElement resource = FasitClient.this.findResource(alias, name, environment, ResourceTypeDO.Queue);

            String queueName = resource.getPropertyString("queueName");
            String manager   = resource.getPropertyString("queueManager");

            return new Queue(queueName, manager);
        }

    }
}

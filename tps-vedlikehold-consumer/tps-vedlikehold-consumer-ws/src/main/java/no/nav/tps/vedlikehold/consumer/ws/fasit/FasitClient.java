package no.nav.tps.vedlikehold.consumer.ws.fasit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import no.nav.aura.envconfig.client.DomainDO;
import no.nav.aura.envconfig.client.FasitRestClient;
import no.nav.aura.envconfig.client.ResourceTypeDO;
import no.nav.aura.envconfig.client.rest.ResourceElement;
import no.nav.tps.vedlikehold.domain.ws.fasit.Queue;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;

import java.util.concurrent.TimeUnit;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class FasitClient {

    private FasitRestClient restClient;

    private Cache<String, ResourceElement> cache;


    public FasitClient(String baseUrl, String username, String password) {
        this.restClient = new FasitRestClient(baseUrl, username, password);

        /* Use a custom cache */
        this.cache = CacheBuilder.newBuilder()
                                 .maximumSize(100)
                                 .expireAfterWrite(10, TimeUnit.MINUTES)
                                 .build();

        this.restClient.useCache(false);                                                // The rest client's cache is never updated
    }

    /* Queues */

    private Queue findQueue(String alias, String applicationName, String environment) {
        ResourceElement resource = this.findResource(alias, applicationName, environment, ResourceTypeDO.Queue);

        String name = resource.getPropertyString("queueName");
        String manager = resource.getPropertyString("queueManager");

        return new Queue(name, manager);
    }

    private QueueManager findQueueManager(String alias, String applicationName, String environment) {
        ResourceElement resource = this.findResource(alias, applicationName, environment, ResourceTypeDO.QueueManager);

        String name     = resource.getPropertyString("name");
        String hostname = resource.getPropertyString("hostname");
        String port     = resource.getPropertyString("port");

        return new QueueManager(name, hostname, port);
    }

    private ResourceElement findResource(String alias, String applicationName, String environment, ResourceTypeDO type) {
        ResourceElement resource = getFromCache(alias, applicationName, environment, type);

        if (resource != null) {
            return resource;
        }

        DomainDO domain = FasitUtils.domainFor(environment);

        resource = this.restClient.getResource(environment, alias, type, domain, applicationName);

        addToCache(resource, alias, applicationName, environment, type);

        return resource;
    }

    /* Application */

    public Application getApplication(String name, String environment) {
        return new Application(name, environment);
    }

    public class Application {
        String environment;
        String name;

        private Application(String name, String environment) {
            this.name = name;
            this.environment = environment;
        }

        public QueueManager getQueueManager(String alias) {
            return FasitClient.this.findQueueManager(alias, name, environment);
        }

        public Queue getQueue(String alias) {
            return FasitClient.this.findQueue(alias, name, environment);
        }
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
        return environment + "." + applicationName + "." + alias + "." + type.name();
    }
}

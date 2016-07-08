package no.nav.tps.vedlikehold.consumer.ws.fasit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import no.nav.aura.envconfig.client.FasitRestClient;
import no.nav.aura.envconfig.client.ResourceTypeDO;
import no.nav.aura.envconfig.client.rest.ResourceElement;

import java.util.concurrent.TimeUnit;

/**
 * Created by Øyvind Grimnes, Visma Consulting AS on 07.07.2016.
 */
public class FasitClient {
    public static final int DEFAULT_CACHETIME = 600;
    protected FasitRestClient restClient;
    private final String baseUrl;
    private final String username;
    private final String password;
    private int cachetime = 600;

    private Cache<String, ResourceElement> cache;

    public FasitClient(String baseUrl, String username, String password) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;

        this.restClient = new FasitRestClient(baseUrl, username, password);

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    public Queue findQueue(String alias, String applicationName, String environment) {
        ResourceElement resource = this.findResource(alias, applicationName, environment, ResourceTypeDO.Queue);

        String name = resource.getPropertyString("queueName");
        String manager = resource.getPropertyString("queueManager");

        return new Queue(name, manager);
    }

    public QueueManager findQueueManager(String alias, String applicationName, String environment) {
        ResourceElement resource = this.findResource(alias, applicationName, environment, ResourceTypeDO.QueueManager);

        String name     = resource.getPropertyString("name");
        String hostname = resource.getPropertyString("hostname");
        String port     = resource.getPropertyString("port");

        return new QueueManager(name, hostname, port);
    }

    public ResourceElement findResource(String alias, String applicationName, String environment, ResourceTypeDO type) {
        ResourceElement resource = getFromCache(alias, applicationName, environment, type);

        if (resource != null) {
            return resource;
        }

        resource = this.restClient.getResource(environment, alias, type, FasitUtils.domainFor(environment),applicationName);
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
        return environment + "." + applicationName + "." + alias + "." + type.name();
    }



    public Application getApplication(String name, String environment) {
        return new Application(name, environment);
    }



    public class Application {
        String environment;
        String name;

        public Application(String name, String environment) {
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

    public class Queue {
        private String name;
        private String manager;

        public Queue(String name, String manager) {
            this.name = name;
            this.manager = manager;
        }

        public String getName() {
            return name;
        }

        public String getManager() {
            return manager;
        }
    }

    public class QueueManager {
        private String name;
        private String hostname;
        private String port;

        public QueueManager(String name, String hostname, String port) {
            this.name = name;
            this.hostname = hostname;
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public String getHostname() {
            return hostname;
        }

        public String getPort() {
            return port;
        }
    }
}

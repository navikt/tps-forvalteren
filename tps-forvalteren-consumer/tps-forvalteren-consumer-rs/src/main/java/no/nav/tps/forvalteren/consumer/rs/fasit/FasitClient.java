package no.nav.tps.forvalteren.consumer.rs.fasit;

import static no.nav.tps.forvalteren.consumer.rs.fasit.config.FasitConstants.QUEUE_MANAGER_ALIAS;
import static no.nav.tps.forvalteren.consumer.rs.fasit.config.FasitConstants.TPSF_FASIT_APP_NAME;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import no.nav.aura.envconfig.client.DomainDO;
import no.nav.aura.envconfig.client.FasitRestClient;
import no.nav.aura.envconfig.client.ResourceTypeDO;
import no.nav.aura.envconfig.client.rest.ResourceElement;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants;
import no.nav.tps.forvalteren.domain.ws.fasit.Queue;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;

public class FasitClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FasitClient.class);

    private static final long CACHE_MAX_SIZE = 100;
    private static final long CACHE_MINUTES_TO_LIVE = 30;
    private static final String DEFAULT_ENVIRONMENT_NUMBER = "6";
    private static final String PING_QUEUE_MANAGER_ALIAS = "mqGateway";
    private static final ResourceTypeDO PING_TYPE = ResourceTypeDO.QueueManager;
    private static final String PING_APPLICATION_NAME = "tpsws";
    private static final String PREFIX_MQ_QUEUES = "QA.";
    private static final String MID_PREFIX_QUEUE_ENDRING = "_412.";
    private static final String MID_PREFIX_QUEUE_HENTING = "_411.";
    private static final String DEV_ENVIRONMENT = "D8";

    private FasitRestClient restClient;

    private Cache<String, ResourceElement> cache;

    @Value("${tpsf.environment.name}")
    private String deployedEnvironment;

    @Value("${mqgateway.hostname}")
    private String mqHostname;

    @Value("${mqgateway.port}")
    private String mqPort;

    @Value("${mqgateway.name}")
    private String mqManagerName;

    public FasitClient(String baseUrl, String username, String password) {
        this.restClient = new FasitRestClient(baseUrl, username, password);

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(CACHE_MAX_SIZE)
                .expireAfterWrite(CACHE_MINUTES_TO_LIVE, TimeUnit.MINUTES)
                .build();

        this.restClient.useCache(false); // The rest client's cache is never updated
    }

    private ResourceElement findResource(String alias, String applicationName, String environment, ResourceTypeDO type) {
        ResourceElement resource = getFromCache(alias, applicationName, environment, type);

        if (resource != null) {
            return resource;
        }

        DomainDO domain = FasitUtilities.domainFor(environment);

        resource = this.restClient.getResource(environment, alias, type, domain, applicationName);

        addToCache(resource, alias, applicationName, environment, type);

        return resource;
    }

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

    public QueueManager getQueueManager(String environment) {
        ResourceElement resource = findResource(QUEUE_MANAGER_ALIAS, TPSF_FASIT_APP_NAME, environment, ResourceTypeDO.QueueManager);

        String managerName = resource.getPropertyString("name");
        String hostname    = resource.getPropertyString("hostname");
        String port        = resource.getPropertyString("port");

        return new QueueManager(managerName, hostname, port);
    }

    public Queue getQueue(String alias, String environment) {
        String queueName = getQueueName(alias, environment);
        return new Queue(queueName, null);
    }

    private String getQueueName(String alias, String environment) {
        String environmentForQueueName = environment;
        if (environment.toUpperCase().contains("U")) {
            environmentForQueueName = DEV_ENVIRONMENT;
        }

        if (TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS.equals(alias)) {
            return PREFIX_MQ_QUEUES + environmentForQueueName.toUpperCase() + MID_PREFIX_QUEUE_HENTING + alias;
        } else {
            return PREFIX_MQ_QUEUES + environmentForQueueName.toUpperCase() + MID_PREFIX_QUEUE_ENDRING + alias;
        }
    }

     // Ping på isReady()
    public boolean ping() {
        try {
            String environmentClass = deployedEnvironment.substring(0,1);
            if (!"p".equalsIgnoreCase(deployedEnvironment)) {
                environmentClass = deployedEnvironment + DEFAULT_ENVIRONMENT_NUMBER;
            }

            DomainDO domain = FasitUtilities.domainFor(environmentClass);

            this.restClient.getResource(environmentClass, PING_QUEUE_MANAGER_ALIAS, PING_TYPE, domain, PING_APPLICATION_NAME);
        } catch (RuntimeException exception) {
            LOGGER.warn("Pinging Fasit failed with exception: {}", exception.toString());
            throw exception;
        }
        return true;
    }

}

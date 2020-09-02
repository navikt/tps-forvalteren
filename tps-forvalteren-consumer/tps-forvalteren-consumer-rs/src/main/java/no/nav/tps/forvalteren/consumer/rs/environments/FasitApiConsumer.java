package no.nav.tps.forvalteren.consumer.rs.environments;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toSet;
import static no.nav.tps.forvalteren.common.java.config.CacheConfig.CACHE_FASIT;
import static no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitPropertyTypes.QUEUE_MANAGER;
import static no.nav.tps.forvalteren.consumer.rs.environments.url.FasitUrl.createQueryPatternByParamName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitApplication;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResource;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResourceWithUnmappedProperties;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitUsedResources;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitMQManager;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitPropertyTypes;
import no.nav.tps.forvalteren.consumer.rs.environments.url.FasitUrl;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants;
import no.nav.tps.forvalteren.domain.ws.fasit.Queue;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;

@Service
public class FasitApiConsumer {

    private static final String DEV_ENVIRONMENT = "D8";
    private static final String PREFIX_MQ_QUEUES = "QA.";
    private static final String MID_PREFIX_QUEUE_ENDRING = "_412.";
    private static final String MID_PREFIX_QUEUE_HENTING = "_411.";
    private static final String ZONE = "FSS";
    private static final String FASIT_APP_NAME = "dummy";
    private static final String QUEUE_MANAGER_ALIAS  = "mqGateway";

    @Value(value = "${fasit.url}")
    private String fasitUrl;

    @Autowired
    private MapperFacade mapperFacade;

    private RestTemplate restTemplate;

    public FasitApiConsumer(RestTemplate template) {
        restTemplate = template;
        restTemplate.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
    }

    public Set<String> getEnvironments(String application) {
        return getEnvironments(application, true);
    }

    private Set<String> getEnvironments(String application, boolean usage) {
        Collection<FasitApplication> applications = getApplicationInstances(application, usage);

        return applications.stream()
                .map(FasitApplication::getEnvironment)
                .collect(toSet());
    }

    public List<FasitUsedResources> getUsedResourcesFromAppByTypes(FasitApplication app, FasitPropertyTypes... fasitProperties) {
        List<String> propertyName = new ArrayList<>(app.getUsedresources().size());

        for (FasitPropertyTypes fasitProperty : fasitProperties) {
            propertyName.add(fasitProperty.getPropertyName());
        }

        return app.getUsedresources().stream()
                .filter(resources -> propertyName.contains(resources.getType()))
                .collect(Collectors.toList());
    }

    @Cacheable(CACHE_FASIT)
    public List<FasitApplication> getApplications(String name) {
        String urlPattern = FasitUrl.APPLICATIONS_V2_GET.getUrl() + createQueryPatternByParamName("name", "pr_page");
        String url = String.format(urlPattern, fasitUrl, name, 1000);

        ResponseEntity<FasitApplication[]> applications = restTemplate.getForEntity(url, FasitApplication[].class);

        return newArrayList(applications.getBody());
    }

    @Cacheable(CACHE_FASIT)
    public List<FasitApplication> getApplicationInstances(String application, boolean usage) {
        String urlPattern = FasitUrl.APPLICATIONINSTANCES_V2_GET.getUrl() + createQueryPatternByParamName("application", "usage");
        String url = String.format(urlPattern, fasitUrl, application, true);

        ResponseEntity<FasitApplication[]> applications = restTemplate.getForEntity(url, FasitApplication[].class);

        return newArrayList(applications.getBody());
    }

    @Cacheable(CACHE_FASIT)
    public List<FasitResource> getResourcesByAliasAndType(String alias, FasitPropertyTypes propertyTypes) {
        String urlPattern = FasitUrl.RESOURCES_V2_GET.getUrl() + createQueryPatternByParamName("alias", "type");
        String url = String.format(urlPattern, fasitUrl, alias, propertyTypes.getPropertyName());

        ResponseEntity<FasitResourceWithUnmappedProperties[]> properties = restTemplate.getForEntity(url, FasitResourceWithUnmappedProperties[].class);

        return mapperFacade.mapAsList(properties.getBody(), FasitResource.class);
    }

    @Cacheable(CACHE_FASIT)
    public List<FasitResource> getResourcesByAliasAndTypeAndEnvironment(String alias, FasitPropertyTypes propertyTypes, String environment) {
        String urlPattern = FasitUrl.RESOURCES_V2_GET.getUrl() + createQueryPatternByParamName("alias", "type", "environment" +
                (environment.length() == 1 ? "class" : ""));
        String url = String.format(urlPattern, fasitUrl, alias, propertyTypes.getPropertyName(), environment);

        ResponseEntity<FasitResourceWithUnmappedProperties[]> properties = restTemplate.getForEntity(url, FasitResourceWithUnmappedProperties[].class);

        return mapperFacade.mapAsList(properties.getBody(), FasitResource.class);
    }

    @Cacheable(CACHE_FASIT)
    public FasitResource getScopedResource(String alias, FasitPropertyTypes propertyTypes, String environment) {
        String urlPattern = FasitUrl.SCOPED_RESOURCE_V2_GET.getUrl() +
                createQueryPatternByParamName("alias", "type", "environment", "application", "zone");

        String url = String.format(urlPattern, fasitUrl, alias, propertyTypes.getPropertyName(), environment, FASIT_APP_NAME, ZONE);

        ResponseEntity<FasitResourceWithUnmappedProperties> properties = restTemplate.getForEntity(url, FasitResourceWithUnmappedProperties.class);

        return mapperFacade.map(properties.getBody(), FasitResource.class);
    }

    @Cacheable(CACHE_FASIT)
    public FasitResource getResourceFromRef(String refurl) {
        ResponseEntity<FasitResourceWithUnmappedProperties> resource = restTemplate.getForEntity(refurl, FasitResourceWithUnmappedProperties.class);
        return mapperFacade.map(resource.getBody(), FasitResource.class);
    }

    @Cacheable(CACHE_FASIT)
    public QueueManager getQueueManager(String environment) {

        FasitResource fasitResource = getScopedResource(QUEUE_MANAGER_ALIAS, QUEUE_MANAGER, environment);
        FasitMQManager mqManager = (FasitMQManager) fasitResource.getProperties();
        return QueueManager.builder()
                .name(mqManager.getName())
                .hostname(mqManager.getHostname())
                .port(mqManager.getPort())
                .build();
    }

    public Queue getQueue(String alias, String environment) {

        return Queue.builder()
                .name(new StringBuilder()
                        .append(PREFIX_MQ_QUEUES)
                        .append(environment.toLowerCase().contains("u") ? DEV_ENVIRONMENT : environment.toUpperCase())
                        .append(TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS.equals(alias) ?
                                MID_PREFIX_QUEUE_HENTING : MID_PREFIX_QUEUE_ENDRING)
                        .append(alias)
                        .toString())
                .build();
    }
}

package no.nav.tps.forvalteren.consumer.rs.environments;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toSet;
import static no.nav.tps.forvalteren.common.java.config.CacheConfig.CACHE_FASIT;
import static no.nav.tps.forvalteren.consumer.rs.environments.url.FasitUrl.createQueryPatternByParamName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitApplication;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResource;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResourceWithUnmappedProperties;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitUsedResources;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitPropertyTypes;
import no.nav.tps.forvalteren.consumer.rs.environments.url.FasitUrl;

@Component
public class FasitApiConsumer {

    protected static final String BASE_URL = "https://fasit.adeo.no";

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
        String url = String.format(urlPattern, BASE_URL, name, 1000);

        ResponseEntity<FasitApplication[]> applications = restTemplate.getForEntity(url, FasitApplication[].class);

        return newArrayList(applications.getBody());
    }

    @Cacheable(CACHE_FASIT)
    public List<FasitApplication> getApplicationInstances(String application, boolean usage) {
        String urlPattern = FasitUrl.APPLICATIONINSTANCES_V2_GET.getUrl() + createQueryPatternByParamName("application", "usage");
        String url = String.format(urlPattern, BASE_URL, application, true);

        ResponseEntity<FasitApplication[]> applications = restTemplate.getForEntity(url, FasitApplication[].class);

        return newArrayList(applications.getBody());
    }

    @Cacheable(CACHE_FASIT)
    public List<FasitResource> getResourcesByAliasAndType(String alias, FasitPropertyTypes propertyTypes) {
        String urlPattern = FasitUrl.RESOURCES_V2_GET.getUrl() + createQueryPatternByParamName("alias", "type");
        String url = String.format(urlPattern, BASE_URL, alias, propertyTypes.getPropertyName());

        ResponseEntity<FasitResourceWithUnmappedProperties[]> properties = restTemplate.getForEntity(url, FasitResourceWithUnmappedProperties[].class);

        return mapperFacade.mapAsList(properties.getBody(), FasitResource.class);
    }

    @Cacheable(CACHE_FASIT)
    public List<FasitResource> getResourcesByAliasAndTypeAndEnvironment(String alias, FasitPropertyTypes propertyTypes, String environment) {
        String urlPattern = FasitUrl.RESOURCES_V2_GET.getUrl() + createQueryPatternByParamName("alias", "type", "environment");
        String url = String.format(urlPattern, BASE_URL, alias, propertyTypes.getPropertyName(), environment);

        ResponseEntity<FasitResourceWithUnmappedProperties[]> properties = restTemplate.getForEntity(url, FasitResourceWithUnmappedProperties[].class);

        return mapperFacade.mapAsList(properties.getBody(), FasitResource.class);
    }

    @Cacheable(CACHE_FASIT)
    public FasitResource getResourceFromRef(String refurl) {
        ResponseEntity<FasitResourceWithUnmappedProperties> resource = restTemplate.getForEntity(refurl, FasitResourceWithUnmappedProperties.class);
        return mapperFacade.map(resource.getBody(), FasitResource.class);
    }
}

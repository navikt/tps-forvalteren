package no.nav.tps.forvalteren.consumer.rs.environments;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitApplication;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResource;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResourceWithUnmappedProperties;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitUsedResources;
import no.nav.tps.forvalteren.consumer.rs.environments.url.FasitUrl;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitPropertyTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toSet;
import static no.nav.tps.forvalteren.consumer.rs.environments.url.FasitUrl.createQueryPatternByParamName;

@Component
public class FasitApiConsumerImpl implements FasitApiConsumer {

    @Autowired
    MapperFacade mapperFacade;

    protected static final String BASE_URL = "https://fasit.adeo.no/api/v2";
    protected static final String BASE_URL_F = "https://fasit.adeo.no";

    private RestTemplate template = new RestTemplate();

    public FasitApiConsumerImpl() {
        List<HttpMessageConverter<?>> messageConverters = singletonList(new MappingJackson2HttpMessageConverter());
        template.setMessageConverters(messageConverters);
    }

    /* Get environments */
    @Override
    public Set<String> getEnvironments(String application) {
        return getEnvironments(application, true);
    }

    private Set<String> getEnvironments(String application, boolean usage) {
        Collection<FasitApplication> applications = getApplications(application, usage);

        return applications.stream()
                .map(FasitApplication::getEnvironment)
                .collect(toSet());
    }

    @Override
    public List<FasitUsedResources> getUsedResourcesFromAppByTypes(FasitApplication app, FasitPropertyTypes... fasitProperties) {
        List<String> propertyName = new ArrayList<>();

        for (FasitPropertyTypes fasitProperty : fasitProperties) {
            propertyName.add(fasitProperty.getPropertyName());
        }

        return app.getUsedresources().stream()
                .filter(resources -> propertyName.contains(resources.getType()))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable("fasit")
    public List<FasitApplication> getApplications(String application, boolean usage) {
        String urlPattern = FasitUrl.APPLICATIONINSTANCES_V2_GET.getUrl() + createQueryPatternByParamName("application", "usage");
        String url = String.format(urlPattern, BASE_URL_F, application, true);

        ResponseEntity<FasitApplication[]> applications = template.getForEntity(url, FasitApplication[].class);

        return Arrays.asList(applications.getBody());
    }

    @Override
    @Cacheable("fasit")
    public List<FasitResource> getResourcesByAliasAndType(String alias, FasitPropertyTypes propertyTypes) {
        String urlPattern = FasitUrl.RESOURCES_V2_GET.getUrl() + createQueryPatternByParamName("alias", "type");
        String url = String.format(urlPattern, BASE_URL_F, alias, propertyTypes.getPropertyName());

        ResponseEntity<FasitResourceWithUnmappedProperties[]> properties = template.getForEntity(url, FasitResourceWithUnmappedProperties[].class);

        return mapperFacade.mapAsList(properties.getBody(), FasitResource.class);
    }

    @Override
    @Cacheable("fasit")
    public FasitResource getResourceFromRef(String refurl){
        ResponseEntity<FasitResourceWithUnmappedProperties> resource = template.getForEntity(refurl, FasitResourceWithUnmappedProperties.class);
        return mapperFacade.map(resource.getBody(), FasitResource.class);
    }
}

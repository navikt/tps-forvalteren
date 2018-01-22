package no.nav.tps.forvalteren.consumer.rs.environments;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Component
public class FetchEnvironmentsManager implements FetchEnvironmentsConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FetchEnvironmentsManager.class);

    protected static final String BASE_URL = "https://fasit.adeo.no/api/v2";

    private RestTemplate template = new RestTemplate();

    public FetchEnvironmentsManager() {
        List<HttpMessageConverter<?>> messageConverters = singletonList(new MappingJackson2HttpMessageConverter());
        template.setMessageConverters(messageConverters);
    }

    /* Get environments */
    @Override
    public Set<String> getEnvironments(String application) {
        return getEnvironments(application, true);
    }

    @Override
    public Set<String> getEnvironments(String application, boolean usage) {
        Collection<FetchEnvironmentsApplication> applications = getApplications(application, usage);

        return applications.stream()
                .map(FetchEnvironmentsApplication::getEnvironment)
                .collect(toSet());
    }

    private Collection<FetchEnvironmentsApplication> getApplications(String application, boolean usage) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("application", application);
        parameters.put("usage", usage);

        String url = buildUrl(Service.DEPLOYLOG, parameters);

        FetchEnvironmentsApplication[] applications = template.getForObject(url, FetchEnvironmentsApplication[].class);
        return Arrays.asList(applications);
    }

    private String buildUrl(Service service, Map<String, Object> parameters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL);

        builder.pathSegment(service.getServiceName());

        /* Add all parameters */
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        return builder.build().encode().toUriString();
    }

    private enum Service {
        DEPLOYLOG("applicationinstances");

        private String serviceName;

        Service(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceName() {
            return serviceName;
        }
    }

}

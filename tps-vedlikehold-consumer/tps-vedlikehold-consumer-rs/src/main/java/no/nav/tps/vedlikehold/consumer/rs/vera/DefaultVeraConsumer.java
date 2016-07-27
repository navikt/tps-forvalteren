package no.nav.tps.vedlikehold.consumer.rs.vera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Component
public class DefaultVeraConsumer implements VeraConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultVeraConsumer.class);

    private static final String PING_VERA = "tpws";
    private static final String BASE_URL  = "http://vera.adeo.no/api/v1";

    private RestTemplate template = new RestTemplate();


    public DefaultVeraConsumer() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        template.setMessageConverters(messageConverters);
    }

    /* Get environments */
    @Override
    public Set<String> getEnvironments(String application) {
        return getEnvironments(application, true, true);
    }

    @Override
    public Set<String> getEnvironments(String application, Boolean onlyLatest, Boolean filterUndeployed) {
        Collection<VeraApplication> applications = getApplications(application, onlyLatest, filterUndeployed);

        return applications.stream()
                .map(VeraApplication::getEnvironment)
                .collect(toSet());
    }

    private Collection<VeraApplication> getApplications(String application, Boolean onlyLatest, Boolean filterUndeployed) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("application", application);
        parameters.put("onlyLatest", onlyLatest);
        parameters.put("filterUndeployed", filterUndeployed);

        String url = buildUrl(Service.DEPLOYLOG, parameters);

        VeraApplication[] applications = template.getForObject(url, VeraApplication[].class);

        return Arrays.asList(applications);
    }


    /* Helper methods */

    private String buildUrl(Service service, Map<String, Object> parameters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL);

        builder.pathSegment(service.getServiceName());

        /* Add all parameters */
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        return builder.build().encode().toUriString();
    }


    @Override
    public boolean ping() throws Exception {
        try {
            this.getEnvironments(PING_VERA);
        } catch (Exception exception) {
            LOGGER.warn("An exceptin was encountered while pinging Vera: {}", exception.toString());

            throw exception;
        }
        return true;
    }


    private enum Service {
        DEPLOYLOG("deploylog");

        private String serviceName;

        Service(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceName() {
            return serviceName;
        }
    }
}

package no.nav.tps.vedlikehold.consumer.rs.vera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Component
public class DefaultVeraConsumer implements VeraConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultVeraConsumer.class);

    private static final String PING_VERA = "tpws";
    public static final String BASE_URL = "http://vera.adeo.no/api/v1";
    private RestTemplate template = new RestTemplate();

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
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("application", application);
        parameters.put("onlyLatest", onlyLatest);
        parameters.put("filterUndeployed", filterUndeployed);

        VeraApplication[] applications = getObjects(Service.DEPLOYLOG, parameters, VeraApplication[].class);

        return applicationToEnvironmentsMapper(Arrays.asList(applications));
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

    private <T> T getObjects(Service service, Map<String, Object> parameters, Class<T> type) {
        String url = buildUrl(service, parameters);
        return template.getForObject(url, type);
    }

    private Set<String> applicationToEnvironmentsMapper(List<VeraApplication> veraApplications) {
        return veraApplications.stream()
                .map(VeraApplication::getEnvironment)
                .collect(toSet());
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
}

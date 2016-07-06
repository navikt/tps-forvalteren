package no.nav.tps.vedlikehold.consumer.rs.vera;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import static java.util.stream.Collectors.toList;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class VeraConsumer {

    private RestTemplate template = new RestTemplate();
    public static final String GET_ENVS_URL = "http://vera.adeo.no/api/v1/deploylog?onlyLatest=true&filterUndeployed=true&application=tpsws";

    public VeraConsumer() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        template.setMessageConverters(messageConverters);
    }

    public List<String> listEnvs() {
        VeraApplication[] applications = template.getForObject(GET_ENVS_URL, VeraApplication[].class);
        return veraApplicationToEnvListMapper(Arrays.asList(applications));
    }

    private List<String> veraApplicationToEnvListMapper(List<VeraApplication> veraApplications) {
        return veraApplications.stream().sorted()
                .map(VeraApplication::getEnvironment)
                .collect(toList());
    }

}

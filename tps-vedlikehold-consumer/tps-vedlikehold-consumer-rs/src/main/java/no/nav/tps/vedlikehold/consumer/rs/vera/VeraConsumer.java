package no.nav.tps.vedlikehold.consumer.rs.vera;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;



import static java.util.stream.Collectors.toList;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class VeraConsumer {

    @Inject
    private RestTemplate template;
    public static final String GET_ENVS_URL = "http://vera.adeo.no/api/v1/deploylog?onlyLatest=true&filterUndeployed=true&application=tpsw";

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

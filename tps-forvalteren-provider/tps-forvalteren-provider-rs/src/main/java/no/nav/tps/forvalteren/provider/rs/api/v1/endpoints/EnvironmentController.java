package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.HashSet;
import java.util.Set;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.provider.rs.api.v1.utils.EnvironmentsFilter;
import no.nav.tps.forvalteren.provider.rs.config.ProviderConstants;
import no.nav.tps.forvalteren.service.command.vera.GetEnvironments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "api/v1")
public class EnvironmentController {

    private static final String REST_SERVICE_NAME = "environments";

    @Autowired
    private GetEnvironments getEnvironmentsCommand;

    @Value("${tps.forvalteren.production-mode}")
    private boolean currentEnvironmentIsProd;

    @Value("${environment.class}")
    private String deployedEnvironment;

    /**
     * Get a set of available environments from Vera
     *
     * @return a set of environment names
     */
    @LogExceptions
    @Metrics(value = "provider", tags = {@Metrics.Tag(key = ProviderConstants.RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = ProviderConstants.OPERATION, value = "getEnvironments")})
    @RequestMapping(value = "/environments", method = RequestMethod.GET)
    public Set<String> getEnvironments() {
        if(currentEnvironmentIsProd){
            Set<String> environments = new HashSet<>();
            environments.add("p");
            return environments;
        } else {
            Set<String> environments = getEnvironmentsCommand.getEnvironmentsFromVera("tpsws");
            return filterEnvironments(environments);
        }
    }

    private Set<String> filterEnvironments(Set<String> environments){
        char env = deployedEnvironment.charAt(0);
        switch (env){
            case 'u':
                return EnvironmentsFilter.create()
                        .include("u*")
                        .include("t*")
                        .exception("t7")                // The queue manager channel 'T7_TPSWS' for this env does not exist
                        .filter(environments);
            case 't':
                return EnvironmentsFilter.create()
                        .include("u*")
                        .include("t*")
                        .exception("t7")                // The queue manager channel 'T7_TPSWS' for this env does not exist
                        .filter(environments);
            case 'q':
                return EnvironmentsFilter.create()
                        .include("q*")
                        .include("u*")
                        .include("t*")
                        .exception("t7")                // The queue manager channel 'T7_TPSWS' for this env does not exist
                        .filter(environments);
            case 'p':
                return EnvironmentsFilter.create()
                        .include("p*")
                        .filter(environments);
            default:
                return EnvironmentsFilter.create()
                        .include("u*")
                        .include("t*")
                        .exception("t7")                // The queue manager channel 'T7_TPSWS' for this env does not exist
                        .filter(environments);
        }
    }
}

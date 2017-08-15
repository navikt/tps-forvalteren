package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.service.environment.Environment;
import no.nav.tps.forvalteren.provider.rs.config.ProviderConstants;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.vera.GetEnvironments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
@RequestMapping(value = "api/v1")
public class EnvironmentController {

    private static final String REST_SERVICE_NAME = "environments";

    @Autowired
    private GetEnvironments getEnvironmentsCommand;

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @Value("${tps.forvalteren.production-mode}")
    private boolean currentEnvironmentIsProd;
    /**
     * Get a set of available environments from Vera
     *
     * @return a set of environment names
     */
    @LogExceptions
    @Metrics(value = "provider", tags = {@Metrics.Tag(key = ProviderConstants.RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = ProviderConstants.OPERATION, value = "getEnvironments")})
    @RequestMapping(value = "/environments", method = RequestMethod.GET)
    public Environment getEnvironments() {
        Set<String> env = getEnvironmentsCommand.getEnvironmentsFromVera("tpsws");

        Environment environment = new Environment();
        environment.setEnvironments(filterEnvironmentsOnDeployedEnvironment.execute(env));
        environment.setProductionMode(currentEnvironmentIsProd);

        return environment;
    }
}

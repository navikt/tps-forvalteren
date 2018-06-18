package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.service.environment.Environment;
import no.nav.tps.forvalteren.provider.rs.config.ProviderConstants;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;
import no.nav.tps.forvalteren.service.user.UserContextHolder;


@RestController
@RequestMapping(value = "api/v1")
public class EnvironmentController {

    private static final String REST_SERVICE_NAME = "environments";

    @Autowired
    private GetEnvironments getEnvironmentsCommand;

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @Autowired
    private UserContextHolder userContextHolder;

    @Value("${tps.forvalteren.production-mode}")
    private boolean currentEnvironmentIsProd;

    /**
     * Get a set of available environments from Fasit
     *
     * @return a set of environment names
     */
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = ProviderConstants.RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = ProviderConstants.OPERATION, value = "getEnvironments") })
    @RequestMapping(value = "/environments", method = RequestMethod.GET)
    public Environment getEnvironments() {
        Set<String> env = getEnvironmentsCommand.getEnvironmentsFromFasit("tpsws");

        Environment environment = new Environment();
        environment.setEnvironments(filterEnvironmentsOnDeployedEnvironment.execute(env));
        environment.setProductionMode(currentEnvironmentIsProd);

        //TODO testing. Dette bør ikke gis i prod! må gjøre om.
        if(!currentEnvironmentIsProd){
            Set<String> rolesA =new HashSet<>();
            rolesA.addAll(Arrays.asList("ROLE_TPSF_SKRIV","ROLE_TPSF_SERVICERUTINER","ROLE_TPSF_SKDMELDING","ROLE_ACCESS","ROLE_TPSF_LES", "ROLE_TPSF_UTVIKLER"));
            environment.setRoles(rolesA);
        }
//        Set<String> roles = userContextHolder.getRoles().stream().map(Enum::toString).collect(Collectors.toSet());

//        environment.setRoles(roles);

        return environment;
    }

}

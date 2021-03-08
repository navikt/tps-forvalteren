package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.service.user.UserRole.ROLE_TPSF_AVSPILLER;
import static no.nav.tps.forvalteren.service.user.UserRole.ROLE_TPSF_AVSPILLER_TEAM_REG;
import static no.nav.tps.forvalteren.service.user.UserRole.ROLE_TPSF_LES;
import static no.nav.tps.forvalteren.service.user.UserRole.ROLE_TPSF_SERVICERUTINER;
import static no.nav.tps.forvalteren.service.user.UserRole.ROLE_TPSF_SKDMELDING;
import static no.nav.tps.forvalteren.service.user.UserRole.ROLE_TPSF_SKRIV;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import no.nav.tps.forvalteren.common.logging.LogExceptions;
import no.nav.tps.forvalteren.domain.service.environment.Environment;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.service.user.UserRole;

@RestController
@RequestMapping(value = "api/v1")
public class EnvironmentController {

    private static final String HAS_GT = "hasGT";
    private static final String HAS_TST = "hasTST";
    private static final String HAS_MLD = "hasMLD";
    private static final String HAS_SRV = "hasSRV";
    private static final String HAS_AVS = "hasAVS";
    private static final String HAS_AVSTR = "hasAVSTR";
    private static final String REST_SERVICE_NAME = "environments";

    @Autowired
    private GetEnvironments getEnvironmentsCommand;

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @Autowired
    private UserContextHolder userContextHolder;

    @Value("${tps.forvalteren.production.mode:true}")
    private boolean currentEnvironmentIsProd;

    /**
     * Get a set of available environments from Fasit
     *
     * @return a set of environment names
     */
    @LogExceptions
    @RequestMapping(value = "/environments", method = RequestMethod.GET)
    public Environment getEnvironments() {
        Set<String> env = getEnvironmentsCommand.getEnvironments();

        Environment environment = new Environment();
        environment.setEnvironments(filterEnvironmentsOnDeployedEnvironment.execute(env));
        environment.setProductionMode(currentEnvironmentIsProd);

        if (userContextHolder.getRoles().iterator().hasNext() && userContextHolder.getRoles().iterator().next() instanceof UserRole) {

            Map<String, Boolean> roller = new HashMap<>();
            Set<String> roles = userContextHolder.getRoles().stream().map(Enum::toString).collect(Collectors.toSet());

            if (currentEnvironmentIsProd) {
                roller.put(HAS_GT, roles.contains(ROLE_TPSF_LES.toString()));
                roller.put(HAS_TST, roles.contains(ROLE_TPSF_SKRIV.toString()));

            } else {
                roller.put(HAS_GT, true);
                roller.put(HAS_TST, true);
            }

            roller.put(HAS_MLD, roles.contains(ROLE_TPSF_SKDMELDING.toString()));
            roller.put(HAS_SRV, roles.contains(ROLE_TPSF_SERVICERUTINER.toString()));
            roller.put(HAS_AVS, roles.contains(ROLE_TPSF_AVSPILLER.toString()));
            roller.put(HAS_AVSTR, roles.contains(ROLE_TPSF_AVSPILLER_TEAM_REG.toString()));

            environment.setRoles(roller);
        }

        return environment;
    }
}

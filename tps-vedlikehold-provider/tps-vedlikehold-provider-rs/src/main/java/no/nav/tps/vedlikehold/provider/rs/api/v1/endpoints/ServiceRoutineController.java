package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
@RestController
@RequestMapping(value = "api/v1")
public class ServiceRoutineController {

    @Autowired
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    @Autowired
    private UserContextHolder userContextHolder;

    /**
     * Get an JSONObject containing all implemented ServiceRutiner and their allowed input attributes
     *
     * @return JSONObject as String containing metadata about all ServiceRutiner
     */
    @LogExceptions
    @RequestMapping(value = "/serviceroutine", method = RequestMethod.GET)
    public List<TpsServiceRoutineDefinition> getTpsServiceRutiner() {
        Set<String> userRoles = userContextHolder.getUser().getRoles();
        return getTpsServiceRutinerService.execute().stream()
                .filter(tpsRoutine -> userRoles.containsAll(tpsRoutine.getRequiredRoles()))
                .collect(Collectors.toList());
    }
}

package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import java.util.List;

import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
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

    /**
     * Get an JSONObject containing all implemented ServiceRutiner and their allowed input attributes
     *
     * @return JSONObject as String containing metadata about all ServiceRutiner
     */
    @LogExceptions
    @RequestMapping(value = "/serviceroutine", method = RequestMethod.GET)
    public List<TpsServiceRoutine> getTpsServiceRutiner() {
        return getTpsServiceRutinerService.exectue();
    }
}

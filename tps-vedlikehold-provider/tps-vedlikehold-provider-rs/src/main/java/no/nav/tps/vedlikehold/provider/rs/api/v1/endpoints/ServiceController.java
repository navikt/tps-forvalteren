package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.tps.vedlikehold.service.command.servicerutiner.GetTpsServiceRutinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */

@RestController
@RequestMapping(value = "api/v1")
public class ServiceController {

    @Autowired
    GetTpsServiceRutinerService getTpsServiceRutinerService;

    /**
     * Get an JSONObject containing all implemented ServiceRutiner
     * and their allowed input attriubtes
     *
     * @return JSONObject as String containing metadata about all ServiceRutiner
     */

    @RequestMapping(value = "/service", method = RequestMethod.GET)
    public String getTpsServiceRutiner() {

        return getTpsServiceRutinerService.getTpsServiceRutiner();
    }
}

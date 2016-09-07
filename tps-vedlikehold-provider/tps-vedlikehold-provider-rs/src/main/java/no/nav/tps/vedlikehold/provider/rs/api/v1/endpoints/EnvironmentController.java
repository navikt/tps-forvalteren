package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import java.util.Set;

import no.nav.tps.vedlikehold.provider.rs.api.v1.utils.EnvironmentsFilter;
import no.nav.tps.vedlikehold.service.command.vera.GetEnvironments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@RestController
@RequestMapping(value = "api/v1")
public class EnvironmentController {

    @Autowired
    public GetEnvironments getEnvironmentsCommand;

    /**
     * Get a set of available environments from Vera
     *
     * @return a set of environment names
     */
    @RequestMapping(value = "/environments", method = RequestMethod.GET)
    public Set<String> getEnvironments() {
        Set<String> environments = getEnvironmentsCommand.execute("tpsws");

        return EnvironmentsFilter.create()
                .include("u*")
                .include("t*")
                .exception("t7")                // The queue manager channel 'T7_TPSWS' for this env does not exist
                .filter(environments);
    }
}

package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.tps.vedlikehold.domain.service.ServiceRutineResponse;
import no.nav.tps.vedlikehold.domain.service.User;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.strategies.user.UserContextUserFactoryStrategy;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.servicerutiner.GetTpsServiceRutinerService;
import no.nav.tps.vedlikehold.service.command.authorisation.AuthorisationService;
import no.nav.tps.vedlikehold.service.command.servicerutiner.TpsServiceRutineService;
import no.nav.tps.vedlikehold.service.command.user.DefaultUserFactory;
import no.nav.tps.vedlikehold.service.command.user.UserFactory;
import no.nav.tps.vedlikehold.service.command.user.UserFactoryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Tobias Hansen, Visma Consulting AS
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RestController
@RequestMapping(value = "api/v1")
public class ServiceController {

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private TpsServiceRutineService tpsServiceRutineService;

    @Autowired
    private AuthorisationService authorisationService;

    @Autowired
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    @RequestMapping(value = "/service/{serviceRutinenavn}", method = RequestMethod.GET)
    public ServiceRutineResponse getService(@ApiIgnore HttpSession session,
                                            @RequestParam String environment,
                                            @ApiIgnore @RequestParam Map<String, Object> parameters,
                                            @PathVariable("serviceRutinenavn") String serviceRutineName) throws Exception {

        /* Verify authorisation */
        UserFactory userFactory      = new DefaultUserFactory();
        UserFactoryStrategy strategy = new UserContextUserFactoryStrategy(userContextHolder, session);

        User user                = userFactory.createUser(strategy);
        String fnr               = (String) parameters.get("fnr");
        String mappedEnvironment = mappedEnvironment(environment);                         // Environments in U are mapped to T4

        if (fnr != null && !authorisationService.userIsAuthorisedToReadPersonInEnvironment(user, fnr, mappedEnvironment)) {
            throw new HttpUnauthorisedException("User is not authorized to access the requested data", "api/v1/service/" + serviceRutineName);
        }

        /* Get results from TPS */
        return tpsServiceRutineService.execute(serviceRutineName, parameters, mappedEnvironment);
    }


    /**
     * Get an JSONObject containing all implemented ServiceRutiner
     * and their allowed input attributes
     *
     * @return JSONObject as String containing metadata about all ServiceRutiner
     */

    @RequestMapping(value = "/service", method = RequestMethod.GET)
    public String getTpsServiceRutiner() {
        return getTpsServiceRutinerService.exectue();
    }

    /* Environments in U are mapped to T4 */
    /* TODO: This could be handled more gracefully */
    private String mappedEnvironment(String environment) {
        if (environment == null || environment.isEmpty()) {
            return environment;
        }

        if (environment.charAt(0) == 'u') {
            return "t4";
        }

        return environment;
    }
}

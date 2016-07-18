package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.tps.vedlikehold.domain.service.User;
import no.nav.tps.vedlikehold.domain.service.ServiceRutineResponse;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.strategies.user.UserContextUserFactoryStrategy;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.java.authorisation.AuthorisationService;
import no.nav.tps.vedlikehold.service.java.service.rutine.GetTpsServiceRutineService;
import no.nav.tps.vedlikehold.service.java.user.DefaultUserFactory;
import no.nav.tps.vedlikehold.service.java.user.UserFactory;
import no.nav.tps.vedlikehold.service.java.user.UserFactoryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RestController
@RequestMapping(value = "api/v1")
public class ServiceController {

    @Autowired
    UserContextHolder userContextHolder;

    @Autowired
    GetTpsServiceRutineService getTpsServiceRutineService;

    @Autowired
    AuthorisationService authorisationService;


    @RequestMapping(value = "/service/{serviceRutinenavn}", method = RequestMethod.GET)
    public ServiceRutineResponse getService(@ApiIgnore HttpSession session,
                                            @RequestParam String environment,
                                            @ApiIgnore @RequestParam Map<String, Object> parameters,
                                            @PathVariable("serviceRutinenavn") String serviceRutineName) throws Exception {

        /* Verify authorisation */
        /* TODO: Authorisation needs to be updated when more service rutines are supported (when fnr is not provided) */
        UserFactory userFactory      = new DefaultUserFactory();
        UserFactoryStrategy strategy = new UserContextUserFactoryStrategy(userContextHolder, session);

        User user  = userFactory.createUser(strategy);
        String fnr = (String) parameters.get("fnr");

        if (fnr != null && !authorisationService.userIsAuthorisedToReadPerson(user, fnr)) {
            throw new HttpUnauthorisedException("User is not authorized to access the requested data", "api/v1/service/" + serviceRutineName);
        }

        /* Get results from TPS */
        return getTpsServiceRutineService.execute(serviceRutineName, parameters, environment);
    }
}

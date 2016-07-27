package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;


import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.ServiceRutineResponse;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.TpsServiceRutine;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpInternalServerErrorException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.strategies.user.UserContextUserFactoryStrategy;
import no.nav.tps.vedlikehold.provider.rs.security.logging.Sporingslogger;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.TpsServiceRutineService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories.DefaultServiceRutineMessageFactory;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories.DefaultServiceRutineMessageFactoryStrategy;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories.ServiceRutineMessageFactory;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories.ServiceRutineMessageFactoryStrategy;
import no.nav.tps.vedlikehold.service.command.user.DefaultUserFactory;
import no.nav.tps.vedlikehold.service.command.user.UserFactory;
import no.nav.tps.vedlikehold.service.command.user.UserFactoryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;

/**
 * @author Tobias Hansen, Visma Consulting AS
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@RestController
@RequestMapping(value = "api/v1")
public class ServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private TpsServiceRutineService tpsServiceRutineService;

    @Autowired
    private TpsAuthorisationService tpsAuthorisationService;

    @Autowired
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    private ServiceRutineMessageFactory serviceRutineMessageFactory = new DefaultServiceRutineMessageFactory();

    /**
     * Execute any simple TPS service rutine.
     * An 'environment' parameter is always needed.
     * Other parameters for the various service rutines are defined in 'ServiceRutines.xml'.
     *
     * @param session           current session
     * @param environment       environment in which to contact TPS
     * @param parameters        map with all URL parameters
     * @param serviceRutineName name of the servicerutine to be executed
     *
     * @return                  object containing the raw XML response from TPS, and a JSON object based on the XML
     *
     * @throws HttpUnauthorisedException        if the user is unauthorised to access the requested data, or an
     * @throws HttpInternalServerErrorException an exception was thrown during execution
     */

    @RequestMapping(
            value = "/service/{serviceRutinenavn}",
            method = RequestMethod.GET
    )
    public ServiceRutineResponse getService(@ApiIgnore HttpSession session,
                                            @RequestParam String environment,
                                            @ApiIgnore @RequestParam(required = false) String fnr,
                                            @RequestParam Map<String, Object> parameters,
                                            @PathVariable("serviceRutinenavn") String serviceRutineName) throws HttpException {

        /* Authorise user based on requested data, and the environment */
        if ( !isAuthorised(fnr, environment, session) ) {
            throw new HttpUnauthorisedException("User is not authorized to access the requested data", "api/v1/service/" + serviceRutineName);
        }

        /* Build the request XML */
        ServiceRutineMessageFactoryStrategy messageFactoryStrategy = new DefaultServiceRutineMessageFactoryStrategy(serviceRutineName, parameters);

        String requestMessage = serviceRutineMessageFactory.createMessage(messageFactoryStrategy);

        /* Send request to TPS */
        try {
            ServiceRutineResponse response = tpsServiceRutineService.execute(requestMessage, environment);

            Sporingslogger.log(environment, serviceRutineName, fnr);

            return response;
        } catch (Exception exception) {
            LOGGER.error("Failed to execute '{}' in environment '{}' with exception: {}",
                    serviceRutineName,
                    environment,
                    exception.toString());

            throw new HttpInternalServerErrorException(exception, "api/v1/service");
        }
    }


    /**
     * Get an JSONObject containing all implemented ServiceRutiner
     * and their allowed input attributes
     *
     * @return JSONObject as String containing metadata about all ServiceRutiner
     */

    @RequestMapping(value = "/service", method = RequestMethod.GET)
    public Collection<TpsServiceRutine> getTpsServiceRutiner() {
        return getTpsServiceRutinerService.exectue();
    }


    private Boolean isAuthorised(String fnr, String environment, HttpSession session) {
        if (fnr == null) {
            return true;
        }

        UserFactory userFactory      = new DefaultUserFactory();
        UserFactoryStrategy strategy = new UserContextUserFactoryStrategy(userContextHolder, session);

        User user = userFactory.createUser(strategy);

        return tpsAuthorisationService.userIsAuthorisedToReadPersonInEnvironment(user, fnr, environment);
    }
}

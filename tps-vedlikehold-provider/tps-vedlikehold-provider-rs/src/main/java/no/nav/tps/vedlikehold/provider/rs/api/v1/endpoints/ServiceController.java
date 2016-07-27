package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.ServiceRutineResponse;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.TpsServiceRutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequest;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpBadRequestException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpInternalServerErrorException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.strategies.user.UserContextUserFactoryStrategy;
import no.nav.tps.vedlikehold.provider.rs.api.v1.utils.RequestClassService;
import no.nav.tps.vedlikehold.provider.rs.security.logging.Sporingslogger;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.TpsServiceRutineService;
import no.nav.tps.vedlikehold.service.command.user.DefaultUserFactory;
import no.nav.tps.vedlikehold.service.command.user.UserFactory;
import no.nav.tps.vedlikehold.service.command.user.UserFactoryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    private ObjectMapper objectMapper = new ObjectMapper();


    public ServiceController() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }


    @RequestMapping(
            value = "/service/{serviceRutinenavn}",
            method = RequestMethod.GET
    )
    public ServiceRutineResponse getService(@ApiIgnore HttpSession session,
                                            @RequestParam String environment,
                                            @RequestParam(required = false) Map<String, Object> parameters,
                                            @PathVariable("serviceRutinenavn") String serviceRutinenavn) throws HttpException {

        parameters.put("serviceRutinenavn", serviceRutinenavn);

        JsonNode jsonNode = objectMapper.convertValue(parameters, JsonNode.class);

        return getService(session, jsonNode);
    }


    @RequestMapping(
            value = "/service",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ServiceRutineResponse getService(@ApiIgnore HttpSession session,
                                            @RequestBody JsonNode body) throws HttpException {

        /* All requests must provide an environment, and a serivcerutine */
        if (!body.has("environment") || !body.has("serviceRutinenavn")) {
            throw new HttpBadRequestException("Both 'environment' and 'serviceRutinenavn' must be defined in th request body", "api/v1/service");
        }

        String environment       = body.get("environment").asText();
        String serviceRutinenavn = body.get("serviceRutinenavn").asText();
        String fnr               = body.has("fnr") ? body.get("fnr").asText() : null;

        /* Authorise user based on requested data, and the environment */
        if ( !isAuthorised(fnr, environment, session) ) {
            throw new HttpUnauthorisedException("User is not authorized to access the requested data", "api/v1/service/" + serviceRutinenavn);
        }

        /* Prepare the request message */
        TpsRequest request = requestObjectForServiceRutine(serviceRutinenavn, body);

        /* Send the request to TPS */
        try {
            ServiceRutineResponse response = tpsServiceRutineService.execute(request);

            Sporingslogger.log(environment, serviceRutinenavn, fnr);

            return response;
        } catch (Exception exception) {
            LOGGER.error("Failed to execute '{}' in environment '{}' with exception: {}",
                    serviceRutinenavn,
                    environment,
                    exception.toString());

            throw new HttpInternalServerErrorException(exception, "api/v1/service");
        }
    }

    private TpsRequest requestObjectForServiceRutine(String serviceRutinenavn, JsonNode body) throws HttpException {
        Class<? extends TpsRequest> requestClass = RequestClassService.getClassForServiceRutinenavn( serviceRutinenavn );

        return objectMapper.convertValue(body, requestClass);
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

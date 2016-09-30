package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Map;

import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.RsRequestMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpBadRequestException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpInternalServerErrorException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.security.logging.Sporingslogger;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.TpsServiceRutineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Tobias Hansen, Visma Consulting AS
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@RestController
@RequestMapping(value = "api/v1")
public class ServiceController {

    private static final String TPS_SERVICE_ROUTINE_PARAM_NAME = "serviceRutinenavn";

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private TpsServiceRutineService tpsServiceRutineService;

    @Autowired
    private TpsAuthorisationService tpsAuthorisationService;

    @Autowired
    private RsRequestMappingUtils mappingUtils;

    @Autowired
    private MessageProvider messageProvider;

    @LogExceptions
    @RequestMapping(value = "/service/{"+ TPS_SERVICE_ROUTINE_PARAM_NAME +"}", method = RequestMethod.GET)
    public ServiceRoutineResponse getService(@RequestParam(required = false) Map<String, Object> tpsRequestParameters, @PathVariable String serviceRutinenavn) {
        tpsRequestParameters.put(TPS_SERVICE_ROUTINE_PARAM_NAME, serviceRutinenavn);

        JsonNode jsonNode = mappingUtils.convert(tpsRequestParameters, JsonNode.class);
        return getService(jsonNode);
    }

    @LogExceptions
    @RequestMapping(value = "/service", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ServiceRoutineResponse getService(@RequestBody JsonNode body) {
        validateRequest(body);

        String environment = body.get("environment").asText();
        String tpsServiceRutinenavn = body.get(TPS_SERVICE_ROUTINE_PARAM_NAME).asText();
        String fnr = null;
        if(body.has("fnr")){
            fnr = body.get("fnr").asText();
        }

        validateAuthorized(fnr, environment, tpsServiceRutinenavn);

        Sporingslogger.log(environment, tpsServiceRutinenavn, fnr);

        TpsRequestServiceRoutine request = mappingUtils.convertToTpsRequestServiceRoutine(tpsServiceRutinenavn, body);
        ServiceRoutineResponse tpsResponse = sendTpsRequest(request);
        return tpsResponse;
    }

    private void validateRequest(JsonNode body) {
        if (!body.has("environment") || !body.has(TPS_SERVICE_ROUTINE_PARAM_NAME)) {
            throw new HttpBadRequestException(messageProvider.get("rest.service.request.exception.MissingRequiredParams"), "api/v1/service");
        }
    }

    private ServiceRoutineResponse sendTpsRequest(TpsRequestServiceRoutine request) {
        try {
            return tpsServiceRutineService.execute(request);
        } catch (Exception exception) {
            throw new HttpInternalServerErrorException(exception, "api/v1/service");
        }
    }

    // Dette er bare for rettighet til å lese person vel..
    private void validateAuthorized(String fnr, String environment, String serviceRutinenavn) {
        if (!isEmpty(fnr)) {
            User user = userContextHolder.getUser();
            boolean authorized = tpsAuthorisationService.userIsAuthorisedToReadPersonInEnvironment(user, fnr, environment);
            if (!authorized) {
                throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/" + serviceRutinenavn);
            }
        }
    }
}

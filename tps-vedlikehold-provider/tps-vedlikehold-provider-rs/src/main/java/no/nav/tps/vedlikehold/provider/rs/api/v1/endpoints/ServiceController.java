package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Map;

import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRutineResponse;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.RsRequestMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpBadRequestException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpException;
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
    @RequestMapping(value = "/service/{serviceRutinenavn}", method = RequestMethod.GET)
    public ServiceRutineResponse getService(@RequestParam(required = false) Map<String, Object> parameters, @PathVariable String serviceRutinenavn) throws HttpException {
        parameters.put("serviceRutinenavn", serviceRutinenavn);

        JsonNode jsonNode = mappingUtils.convert(parameters, JsonNode.class);
        return getService(jsonNode);
    }

    @LogExceptions
    @RequestMapping(value = "/service", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ServiceRutineResponse getService(@RequestBody JsonNode body) throws HttpException {
        validateRequest(body);

        String environment = body.get("environment").asText();
        String serviceRutinenavn = body.get("serviceRutinenavn").asText();
        String fnr = body.has("fnr") ? body.get("fnr").asText() : null;

        validateAuthorized(fnr, environment, serviceRutinenavn);

        Sporingslogger.log(environment, serviceRutinenavn, fnr);

        TpsRequest request = mappingUtils.convertToTpsRequest(serviceRutinenavn, body);
        return sendRequest(request);
    }

    private void validateRequest(JsonNode body) throws HttpBadRequestException {
        if (!body.has("environment") || !body.has("serviceRutinenavn")) {
            throw new HttpBadRequestException(messageProvider.get("rest.service.request.exception.MissingRequiredParams"), "api/v1/service");
        }
    }

    private ServiceRutineResponse sendRequest(TpsRequest request) throws HttpException {
        try {
            return tpsServiceRutineService.execute(request);
        } catch (Exception exception) {
            throw new HttpInternalServerErrorException(exception, "api/v1/service");
        }
    }

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

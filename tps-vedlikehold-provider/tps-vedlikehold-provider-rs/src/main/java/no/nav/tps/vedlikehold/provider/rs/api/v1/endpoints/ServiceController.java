package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;


import java.io.IOException;
import java.util.Map;

import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsMessage;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.RsRequestMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.TpsResponseMappingUtils;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpBadRequestException;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.security.logging.Sporingslogger;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.vedlikehold.service.command.tps.TpsRequestService;

import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;
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
    private TpsRequestService tpsRequestService;

    @Autowired
    private TpsAuthorisationService tpsAuthorisationService;

    @Autowired
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    @Autowired
    private RsRequestMappingUtils mappingUtils;

    @Autowired
    private TpsResponseMappingUtils tpsResponseMappingUtils;

    @Autowired
    private MessageProvider messageProvider;

    @LogExceptions
    @RequestMapping(value = "/service/{" + TPS_SERVICE_ROUTINE_PARAM_NAME + "}", method = RequestMethod.GET)
    public ServiceRoutineResponse getService(@RequestParam(required = false) Map<String, Object> tpsRequestParameters, @PathVariable String serviceRutinenavn) {
        tpsRequestParameters.put(TPS_SERVICE_ROUTINE_PARAM_NAME, serviceRutinenavn);

        JsonNode jsonNode = mappingUtils.convert(tpsRequestParameters, JsonNode.class);
        return getService(jsonNode);
    }

    @LogExceptions
    @RequestMapping(value = "/service", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ServiceRoutineResponse getService(@RequestBody JsonNode body) {
        validateRequest(body);
        return sendRequest(body);
    }

    private ServiceRoutineResponse sendRequest(JsonNode body) {
        String environment = body.get("environment").asText();
        String tpsServiceRutinenavn = body.get(TPS_SERVICE_ROUTINE_PARAM_NAME).asText();
        if (body.has("fnr")) {
            String fnr = body.get("fnr").asText();
            return sendTpsRequestAndAuthorizeBeforeSendingRequest(tpsServiceRutinenavn, fnr, environment, body);
        }
        return sendTpsRequestAndAuthorizeAfterResponseIsReceived(tpsServiceRutinenavn, environment, body);
    }

    private ServiceRoutineResponse sendTpsRequestAndAuthorizeBeforeSendingRequest(String tpsServiceRutinenavn, String fnr, String environment, JsonNode body) {
        //Kommentert ut fordi flyttet Authoriseringen til Service-laget.
        /*User user = userContextHolder.getUser();
        TpsMessage msgClass = getTpsMessageClass(tpsServiceRutinenavn);
        /*if (!tpsAuthorisationService.userIsAuthorisedToReadPersonInEnvironment(msgClass, user, body)) {
            throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/" + tpsServiceRutinenavn);
        }*/
        Sporingslogger.log(environment, tpsServiceRutinenavn, fnr);
        TpsRequestServiceRoutine tpsRequest = mappingUtils.convertToTpsRequestServiceRoutine(tpsServiceRutinenavn, body);
        return sendTpsRequest(tpsRequest);
    }

    private ServiceRoutineResponse sendTpsRequestAndAuthorizeAfterResponseIsReceived(String tpsServiceRutinenavn, String environment, JsonNode body) {
        Sporingslogger.log(environment, tpsServiceRutinenavn, null);
        TpsRequestServiceRoutine tpsRequest = mappingUtils.convertToTpsRequestServiceRoutine(tpsServiceRutinenavn, body);
        ServiceRoutineResponse tpsResponse = sendTpsRequest(tpsRequest);
        remapTpsResponseExcludingUnauthorizedData(tpsResponse);
        return tpsResponse;
    }

    // Laget kun for testing egentlig. Bare å fjerne ved merge
    private TpsMessage getTpsMessageClass(String serviceRutineNavn){
        TpsMessage msgClass = getTpsServiceRutinerService.execute()
                .stream()
                .filter(request -> request.getName().equalsIgnoreCase(serviceRutineNavn))
                .findFirst().get();
        return msgClass;
    }

    private void remapTpsResponseExcludingUnauthorizedData(ServiceRoutineResponse tpsResponse) {
        try {
            //Kommentert ut for å teste kun authentisering før svar fra Tps
            //TpsMessage msgClass = getTpsMessageClass(tpsResponse.getServiceRoutineName());
            //tpsResponseMappingUtils.removeUnauthorizedDataFromTpsResponse(msgClass, tpsResponse);
            tpsResponseMappingUtils.remapTpsResponse(tpsResponse);
        } catch (HttpUnauthorisedException exception) {
            throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/" + tpsResponse.getServiceRoutineName());
        } catch (IOException exception) {
            throw new HttpInternalServerErrorException(exception, "api/v1/service");
        }
    }

    private void validateRequest(JsonNode body) {
        if (!body.has("environment") || !body.has(TPS_SERVICE_ROUTINE_PARAM_NAME)) {
            throw new HttpBadRequestException(messageProvider.get("rest.service.request.exception.MissingRequiredParams"), "api/v1/service");
        }
    }

    private ServiceRoutineResponse sendTpsRequest(TpsRequestServiceRoutine request) {
        try {
            TpsMessage routine = getTpsMessageClass(request.getServiceRutinenavn());
            String xmlResponse = tpsRequestService.executeServiceRutineRequest(request, routine);
            ServiceRoutineResponse response = tpsResponseMappingUtils.xmlResponseToServiceRoutineResponse(xmlResponse);
            return response;
        }catch (HttpUnauthorisedException ex){
            throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/" + request.getServiceRutinenavn());
        } catch (Exception exception) {
            throw new HttpInternalServerErrorException(exception, "api/v1/service");
        }
    }
}

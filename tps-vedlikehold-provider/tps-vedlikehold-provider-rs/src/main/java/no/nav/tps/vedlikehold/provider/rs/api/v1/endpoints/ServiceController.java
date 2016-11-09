package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;


import com.fasterxml.jackson.databind.JsonNode;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.command.User.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.RsRequestMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.TpsResponseMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.service.command.tps.TpsRequestService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.FindServiceRoutineByName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


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
    private RsRequestMappingUtils mappingUtils;

    @Autowired
    private FindServiceRoutineByName findServiceRoutineByName;

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
//        validateRequest(body);

        String environment = body.get("environment").asText();
        String tpsServiceRutinenavn = body.get(TPS_SERVICE_ROUTINE_PARAM_NAME).asText();
        TpsRequestServiceRoutine tpsRequest = mappingUtils.convertToTpsRequestServiceRoutine(tpsServiceRutinenavn, body);

        return sendTpsRequest(tpsRequest, tpsServiceRutinenavn);
    }
//    private void validateRequest(JsonNode body) {
//        if (!body.has("environment") || !body.has(TPS_SERVICE_ROUTINE_PARAM_NAME)) {
//            throw new HttpBadRequestException(messageProvider.get("rest.service.request.exception.MissingRequiredParams"), "api/v1/service");
//        }
//    }


    private ServiceRoutineResponse sendTpsRequest(TpsRequestServiceRoutine request, String serviceRoutineName) {
        try {
            TpsServiceRoutine serviceRoutine = findServiceRoutineByName.execute(serviceRoutineName).get();

            User user = userContextHolder.getUser();

            Response response = tpsRequestService.executeServiceRutineRequest(request, serviceRoutine, user);
            return tpsResponseMappingUtils.xmlResponseToServiceRoutineResponse(response.getXml());

        } catch (HttpUnauthorisedException ex){
            throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/" + request.getServiceRutinenavn());
        } catch (Exception exception) {
            throw new HttpInternalServerErrorException(exception, "api/v1/service");
        }
    }

}

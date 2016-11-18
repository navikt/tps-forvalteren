package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;


import com.fasterxml.jackson.databind.JsonNode;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.RsTpsRequestMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.RsTpsResponseMappingUtils;
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
    private static final String ENVIRONMENT_PARAM_NAME = "environment";

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private TpsRequestService tpsRequestService;

    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;

    @Autowired
    private FindServiceRoutineByName findServiceRoutineByName;

    @Autowired
    private RsTpsResponseMappingUtils rsTpsResponseMappingUtils;

    @Autowired
    private MessageProvider messageProvider;

    @LogExceptions
    @RequestMapping(value = "/service/{" + TPS_SERVICE_ROUTINE_PARAM_NAME + "}", method = RequestMethod.GET)
    public TpsServiceRoutineResponse getService(@RequestParam(required = false) Map<String, Object> tpsRequestParameters, @PathVariable String serviceRutinenavn) {
        tpsRequestParameters.put(TPS_SERVICE_ROUTINE_PARAM_NAME, serviceRutinenavn);

        JsonNode jsonNode = mappingUtils.convert(tpsRequestParameters, JsonNode.class);
        return getService(jsonNode);
    }

    @LogExceptions
    @RequestMapping(value = "/service", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TpsServiceRoutineResponse getService(@RequestBody JsonNode body) {

        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());
        context.setEnvironment(body.get(ENVIRONMENT_PARAM_NAME).asText());

        String tpsServiceRutinenavn = body.get(TPS_SERVICE_ROUTINE_PARAM_NAME).asText();

        TpsServiceRoutineRequest tpsServiceRoutineRequest = mappingUtils.convertToTpsServiceRoutineRequest(tpsServiceRutinenavn, body);

        return sendTpsRequest(tpsServiceRoutineRequest, context);
    }


    private TpsServiceRoutineResponse sendTpsRequest(TpsServiceRoutineRequest request, TpsRequestContext context) {
        try {
            TpsServiceRoutineDefinition serviceRoutine = findServiceRoutineByName.execute(request.getServiceRutinenavn()).get();

            Response response = tpsRequestService.executeServiceRutineRequest(request, serviceRoutine, context);
            return rsTpsResponseMappingUtils.convertToResponse(response);

        } catch (HttpUnauthorisedException ex){
            throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/" + request.getServiceRutinenavn());
        } catch (Exception exception) {
            throw new HttpInternalServerErrorException(exception, "api/v1/service");
        }
    }

}

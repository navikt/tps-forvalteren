package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.vedlikehold.service.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static no.nav.tps.vedlikehold.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.vedlikehold.provider.rs.config.ProviderConstants.RESTSERVICE;
import static no.nav.tps.vedlikehold.provider.rs.security.logging.Sporingslogger.loggSporing;

@RestController
@RequestMapping(value = "api/v1")
public class ServiceController {

    private static final String REST_SERVICE_NAME = "service";
    private static final String TPS_SERVICE_ROUTINE_PARAM_NAME = "serviceRutinenavn";
    private static final String ENVIRONMENT_PARAM_NAME = "environment";

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;

    @Autowired
    private TpsRequestSender tpsRequestSender;


    @LogExceptions
    @Metrics(value = "provider", tags = {@Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getService")})
    @RequestMapping(value = "/service/{" + TPS_SERVICE_ROUTINE_PARAM_NAME + "}", method = RequestMethod.GET)
    public TpsServiceRoutineResponse getService(@RequestParam(required = false) Map<String, Object> tpsRequestParameters, @PathVariable String serviceRutinenavn) {
        loggSporing(serviceRutinenavn, tpsRequestParameters);

        tpsRequestParameters.put(TPS_SERVICE_ROUTINE_PARAM_NAME, serviceRutinenavn);

        JsonNode body = mappingUtils.convert(tpsRequestParameters, JsonNode.class);

        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());
        context.setEnvironment(body.get(ENVIRONMENT_PARAM_NAME).asText());

        String tpsServiceRutinenavn = body.get(TPS_SERVICE_ROUTINE_PARAM_NAME).asText();
        TpsServiceRoutineRequest tpsServiceRoutineRequest = mappingUtils.convertToTpsServiceRoutineRequest(tpsServiceRutinenavn, body);

        return tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, context);
    }
}

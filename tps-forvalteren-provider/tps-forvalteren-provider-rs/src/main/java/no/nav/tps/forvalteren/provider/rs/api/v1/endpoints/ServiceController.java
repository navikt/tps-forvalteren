package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.Map;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.rs.RsPureXmlMessageResponse;
import no.nav.tps.forvalteren.domain.rs.RsXmlMelding;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;
import no.nav.tps.forvalteren.provider.rs.security.logging.BaseProvider;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.command.tps.xmlmelding.TpsXmlSender;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1")
public class ServiceController extends BaseProvider {

    private static final String REST_SERVICE_NAME = "service";
    private static final String TPS_SERVICE_ROUTINE_PARAM_NAME = "serviceRutinenavn";
    private static final String ENVIRONMENT_PARAM_NAME = "environment";

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;

    @Autowired
    private TpsRequestSender tpsRequestSender;

    @Autowired
    private TpsXmlSender tpsXmlSender;

    @PreAuthorize("hasRole('ROLE_TPSF_SERVICERUTINER')")
    @LogExceptions
    @Metrics(value = "provider", tags = {@Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getService")})
    @RequestMapping(value = "/service/{" + TPS_SERVICE_ROUTINE_PARAM_NAME + "}", method = RequestMethod.GET)
    public TpsServiceRoutineResponse getService(@RequestParam(required = false) Map<String, Object> tpsRequestParameters, @PathVariable String serviceRutinenavn) {
        loggSporing(serviceRutinenavn, tpsRequestParameters);

        tpsRequestParameters.put(TPS_SERVICE_ROUTINE_PARAM_NAME, serviceRutinenavn);

        putFnrIntoRequestParameters(tpsRequestParameters);

        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());
        context.setEnvironment(tpsRequestParameters.get(ENVIRONMENT_PARAM_NAME).toString());

        TpsServiceRoutineRequest tpsServiceRoutineRequest = mappingUtils.convertToTpsServiceRoutineRequest(serviceRutinenavn, tpsRequestParameters);

        return tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, context);
    }


    @PreAuthorize("hasRole('ROLE_TPSF_SKRIV')")
    @LogExceptions
    @Metrics(value = "provider", tags = {@Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "sendXmlMelding")})
    @RequestMapping(value = "/xmlmelding", method = RequestMethod.POST)
    public RsPureXmlMessageResponse sendXmlMelding(@RequestBody RsXmlMelding rsXmlMelding) throws Exception {

        RsPureXmlMessageResponse response = new RsPureXmlMessageResponse();
        response.setXml(tpsXmlSender.sendXml(rsXmlMelding));
        return response;
    }


    private void putFnrIntoRequestParameters(Map<String, Object> tpsRequestParameters) {
        if (tpsRequestParameters.containsKey("nFnr")) {
            String fnrStringListe = (String) tpsRequestParameters.get("nFnr");
            tpsRequestParameters.put("fnr", fnrStringListe);
            if (fnrStringListe.length() > 11) {
                String[] fnr = fnrStringListe.replaceAll("^[,\\s]+", "").split("[,\\s]+");
                tpsRequestParameters.put("fnr", fnr);
            }
        }
    }

}

package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.Map;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.rs.RsPureXmlMessageResponse;
import no.nav.tps.forvalteren.domain.rs.RsTpsMelding;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;
import no.nav.tps.forvalteren.provider.rs.security.logging.BaseProvider;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.GetTpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.tps.xmlmelding.TpsXmlSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    @Autowired
    private GetTpsServiceRoutineResponse getTpsServiceRoutineResponse;

    @Autowired(required = false)
    private TpsXmlSender tpsXmlSender;

    @PreAuthorize("hasRole('ROLE_TPSF_SERVICERUTINER')")
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getService") })
    @RequestMapping(value = "/service/{" + TPS_SERVICE_ROUTINE_PARAM_NAME + "}", method = RequestMethod.GET)
    public TpsServiceRoutineResponse getService(@RequestParam(required = false) Map<String, Object> tpsRequestParameters, @PathVariable String serviceRutinenavn) {
        loggSporing(serviceRutinenavn, tpsRequestParameters);
        putFnrIntoRequestParameters(tpsRequestParameters);

        return getTpsServiceRoutineResponse.execute(serviceRutinenavn, tpsRequestParameters, true);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "sendXmlMelding") })
    @RequestMapping(value = "/xmlmelding", method = RequestMethod.POST)
    @ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
    public RsPureXmlMessageResponse sendXmlMelding(@RequestBody RsTpsMelding rsTpsMelding) throws Exception {

        RsPureXmlMessageResponse response = new RsPureXmlMessageResponse();
        response.setXml(tpsXmlSender.sendTpsMelding(rsTpsMelding));
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

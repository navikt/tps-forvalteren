package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.provider.rs.api.v1.RestAuthorizationService;
import no.nav.tps.forvalteren.provider.rs.security.logging.BaseProvider;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;
import no.nav.tps.forvalteren.service.user.UserRole;

@RestController
@RequestMapping(value = "api/v1")
public class ServiceroutineController extends BaseProvider {

    private static final String REST_SERVICE_NAME = "serviceroutine";
    private static final String TPS_SERVICE_ROUTINE_PARAM_NAME = "serviceRutinenavn";

    @Autowired
    private TpsServiceRoutineService getTpsServiceRoutineResponse;

    @Autowired
    private RestAuthorizationService authorizationService;

    @Value("${tps.forvalteren.production.mode}")
    private boolean currentEnvironmentIsProd;

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getService") })
    @RequestMapping(value = "/" + REST_SERVICE_NAME + "/{" + TPS_SERVICE_ROUTINE_PARAM_NAME + "}", method = RequestMethod.GET)
    public TpsServiceRoutineResponse executeServiceRoutine(@RequestParam(required = false) Map<String, Object> tpsRequestParameters, @PathVariable String serviceRutinenavn) {
        loggSporing(serviceRutinenavn, tpsRequestParameters);

        if (currentEnvironmentIsProd) {
            authorizationService.assertAuthorized(UserRole.ROLE_TPSF_SERVICERUTINER);
        }

        putFnrIntoRequestParameters(tpsRequestParameters);

        return getTpsServiceRoutineResponse.execute(serviceRutinenavn, tpsRequestParameters, true);
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

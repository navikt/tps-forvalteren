package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.Map;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1")
public class NavEndringsmeldingController {

    private static final String TPS_SERVICE_ROUTINE_PARAM_NAME = "serviceRutinenavn";
    private static final String ENVIRONMENT_PARAM_NAME = "environment";

    @Autowired
    private UserContextHolder userContextHolder;

    @RequestMapping(value = "/navmelding/{" + TPS_SERVICE_ROUTINE_PARAM_NAME + "}", method = RequestMethod.GET)
    public TpsServiceRoutineResponse sendMelding(@RequestParam(required = false) Map<String, Object> tpsRequestParameters, @PathVariable String serviceRutinenavn) {

        tpsRequestParameters.put(TPS_SERVICE_ROUTINE_PARAM_NAME, serviceRutinenavn);

        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());
        context.setEnvironment(tpsRequestParameters.get(ENVIRONMENT_PARAM_NAME).toString());



        return null;
    }

}

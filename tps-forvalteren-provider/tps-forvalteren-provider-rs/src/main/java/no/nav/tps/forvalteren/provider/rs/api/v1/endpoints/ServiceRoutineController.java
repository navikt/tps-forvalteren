package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.List;
import java.util.stream.Collectors;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.provider.rs.config.ProviderConstants;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.forvalteren.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.GetTpsServiceRutinerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "api/v1/")
public class ServiceRoutineController {

    private static final String REST_SERVICE_NAME = "serviceroutine";

    @Autowired
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    @Autowired
    private TpsAuthorisationService authorisationService;

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = ProviderConstants.RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = ProviderConstants.OPERATION, value = "getTpsServiceRutiner")})
    @RequestMapping(value = "/serviceroutine", method = RequestMethod.GET)
    public List<TpsServiceRoutineDefinition> getTpsServiceRutiner() {
        return getTpsServiceRutinerService.execute().stream()
                .filter(authorisationService::isAuthorisedToUseServiceRutine)
                .collect(Collectors.toList());
    }

}

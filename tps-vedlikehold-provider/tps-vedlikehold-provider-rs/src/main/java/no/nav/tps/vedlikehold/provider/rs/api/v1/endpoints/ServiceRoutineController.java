package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import java.util.List;
import java.util.stream.Collectors;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static no.nav.tps.vedlikehold.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.vedlikehold.provider.rs.config.ProviderConstants.RESTSERVICE;


@RestController
@RequestMapping(value = "api/v1/")
public class ServiceRoutineController {

    private static final String REST_SERVICE_NAME = "serviceroutine";

    @Autowired
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    @Autowired
    private TpsAuthorisationService authorisationService;

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getTpsServiceRutiner")})
    @RequestMapping(value = "/serviceroutine", method = RequestMethod.GET)
    public List<TpsServiceRoutineDefinition> getTpsServiceRutiner() {
        return getTpsServiceRutinerService.execute().stream()
                .filter(tpsServiceRoutine -> authorisationService.isAuthorisedToUseServiceRutine(tpsServiceRoutine))
                .collect(Collectors.toList());
    }

}

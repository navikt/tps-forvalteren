package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.provider.rs.config.ProviderConstants;
import no.nav.tps.forvalteren.service.command.authorisation.ForbiddenCallHandlerService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.GetTpsServiceRutinerService;

@RestController
@RequestMapping(value = "api/v1/")
@PreAuthorize("hasRole('ROLE_TPSF_SERVICERUTINER')")
public class TpsServicesController {

    private static final String REST_SERVICE_NAME = "tpsservices";

    @Autowired
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    @Autowired
    private ForbiddenCallHandlerService authorisationService;

    @Metrics(value = "provider", tags = { @Metrics.Tag(key = ProviderConstants.RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = ProviderConstants.OPERATION, value = "getTpsServices") })
    @RequestMapping(value = "/" + REST_SERVICE_NAME, method = RequestMethod.GET)
    public List<TpsServiceRoutineDefinitionRequest> getTpsServicesMenu() {
        return getTpsServiceRutinerService.execute().stream()
                .filter(authorisationService::isAuthorisedToUseServiceRutine)
                .collect(Collectors.toList());
    }
}

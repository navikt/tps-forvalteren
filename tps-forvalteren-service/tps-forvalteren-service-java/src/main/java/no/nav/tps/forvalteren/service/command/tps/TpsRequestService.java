package no.nav.tps.forvalteren.service.command.tps;

import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;

@FunctionalInterface
public interface TpsRequestService {
    Response executeServiceRutineRequest(TpsServiceRoutineRequest request, TpsServiceRoutineDefinitionRequest serviceRoutine, TpsRequestContext context) throws Exception;
}

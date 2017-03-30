package no.nav.tps.vedlikehold.service.command.tps;

import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;


public interface TpsRequestService {
    Response executeServiceRutineRequest(TpsServiceRoutineRequest request, TpsServiceRoutineDefinition serviceRoutine, TpsRequestContext context) throws Exception;
}

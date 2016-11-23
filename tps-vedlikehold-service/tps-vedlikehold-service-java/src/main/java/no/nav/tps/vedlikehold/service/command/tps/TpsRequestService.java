package no.nav.tps.vedlikehold.service.command.tps;

import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsServiceRoutineRequest;

/**
 * Created by F148888 on 21.10.2016.
 */

public interface TpsRequestService {
    Response executeServiceRutineRequest(TpsServiceRoutineRequest request, TpsServiceRoutineDefinition serviceRoutine, TpsRequestContext context) throws Exception;
}

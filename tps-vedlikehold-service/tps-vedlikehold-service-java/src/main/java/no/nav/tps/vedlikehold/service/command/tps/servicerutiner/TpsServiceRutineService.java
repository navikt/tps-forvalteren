package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@FunctionalInterface
public interface TpsServiceRutineService {
     ServiceRoutineResponse execute(TpsRequestServiceRoutine request) throws Exception;
}

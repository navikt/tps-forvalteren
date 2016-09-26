package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRutineResponse;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@FunctionalInterface
public interface TpsServiceRutineService {
     ServiceRutineResponse execute(TpsRequest request) throws Exception;
}

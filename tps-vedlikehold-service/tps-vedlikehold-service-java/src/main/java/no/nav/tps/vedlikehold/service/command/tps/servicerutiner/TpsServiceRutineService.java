package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import java.util.Map;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.ServiceRutineResponse;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@FunctionalInterface
public interface TpsServiceRutineService {
     ServiceRutineResponse execute(
            String serviceRutine,
            Map<String, Object> parameters,
            String environment) throws Exception;
}

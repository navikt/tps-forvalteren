package no.nav.tps.vedlikehold.service.java.service.rutine;

import no.nav.tps.vedlikehold.domain.service.ServiceRutineResponse;

import java.util.Map;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public interface GetTpsServiceRutineService {
     ServiceRutineResponse execute(
            String serviceRutine,
            Map<String, Object> parameters,
            String environment) throws Exception;
}

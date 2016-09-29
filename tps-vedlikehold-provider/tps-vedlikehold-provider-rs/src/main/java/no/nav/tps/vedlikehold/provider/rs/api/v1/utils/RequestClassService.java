package no.nav.tps.vedlikehold.provider.rs.api.v1.utils;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsHentPersonRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsPingRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class RequestClassService {

    public static Class<? extends TpsRequestServiceRoutine> getClassForServiceRutinenavn(String serviceRutinenavn) {

        switch (serviceRutinenavn) {
            case "FS03-FDNUMMER-PERSDATA-O":
                return TpsHentPersonRequestServiceRoutine.class;
            case "FS03-OTILGANG-TILSRTPS-O":
                return TpsPingRequestServiceRoutine.class;
            default:
                return TpsRequestServiceRoutine.class;
        }
    }
}

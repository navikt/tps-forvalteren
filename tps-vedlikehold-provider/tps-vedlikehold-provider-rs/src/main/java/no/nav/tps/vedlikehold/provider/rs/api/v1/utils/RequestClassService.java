package no.nav.tps.vedlikehold.provider.rs.api.v1.utils;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsHentPersonRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsPingRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequest;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class RequestClassService {

    public static Class<? extends TpsRequest> getClassForServiceRutinenavn(String serviceRutinenavn) {

        switch (serviceRutinenavn) {
            case "FS03-FDNUMMER-PERSDATA-O":
                return TpsHentPersonRequest.class;
            case "FS03-OTILGANG-TILSRTPS-O":
                return TpsPingRequest.class;
            default:
                return TpsRequest.class;
        }

    }

}

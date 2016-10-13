package no.nav.tps.vedlikehold.provider.rs.api.v1.utils;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.*;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class RequestClassService {

    public static Class<? extends TpsRequestServiceRoutine> getClassForServiceRutinenavn(String serviceRutinenavn) {

        switch (serviceRutinenavn) {
            case "FS03-FDNUMMER-PERSDATA-O":
                return TpsHentPersonRequestServiceRoutine.class;
            case "FS03-FDNUMMER-KONTINFO-O":
                return TpsHentKontaktinformasjonServiceRoutine.class;
            case "FS03-NAADRSOK-PERSDATA-O":
                return TpsSokPersonRequestServiceRoutine.class;
            default:
                return TpsRequestServiceRoutine.class;
        }
    }
}

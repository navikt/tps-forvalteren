package no.nav.tps.forvalteren.service.command.testdata.utils;

import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils.STATUS_KEY;

import java.util.LinkedHashMap;

import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;

public class BehandleTpsRespons {

    private BehandleTpsRespons() {
    }

    public static ResponseStatus ekstraherStatusFraServicerutineRespons(TpsServiceRoutineResponse svar) {
        LinkedHashMap map = (LinkedHashMap) svar.getResponse();
         return (ResponseStatus) map.get(STATUS_KEY);
    }
}

package no.nav.tps.forvalteren.service.command.testdata.utils;

import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils.STATUS_KEY;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import java.util.Map;

import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;

public final class BehandleTpsRespons {

    private BehandleTpsRespons() {
    }

    public static ResponseStatus ekstraherStatusFraServicerutineRespons(TpsServiceRoutineResponse svar) {
        ResponseStatus responseStatus = (ResponseStatus) ((Map) svar.getResponse()).get(STATUS_KEY);
        responseStatus.setMelding(nullcheckSetDefaultValue(responseStatus.getMelding(), null));
        responseStatus.setUtfyllendeMelding(nullcheckSetDefaultValue(responseStatus.getUtfyllendeMelding(), null));
        return responseStatus;
    }
}
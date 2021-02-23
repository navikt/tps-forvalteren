package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.ExceptionTpsServiceRutineMessageCreator;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsServiceRutineException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class TpsResponseToJsonHandler {

    public Map execute(TpsServiceRoutineResponse tpsResponse) {
        ResponseStatus status = (ResponseStatus) ((LinkedHashMap) tpsResponse.getResponse()).get("status");

        if ("00".equals(status.getKode())) {
            return (Map) ((LinkedHashMap) tpsResponse.getResponse()).get("data1");
        }

        if ("08".equals(status.getKode()) && status.getUtfyllendeMelding().contains("finnes ikke")) {
            throw new NotFoundException(status.getUtfyllendeMelding());
        }

        throw new TpsServiceRutineException(ExceptionTpsServiceRutineMessageCreator.execute(status));
    }
}

package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.ExceptionTpsServiceRutineMessageCreator;
import no.nav.tps.forvalteren.service.command.exceptions.TpsServiceRutineException;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TpsResponseToJsonHandler {

    public Map execute(TpsServiceRoutineResponse tpsResponse){
        ResponseStatus status = (ResponseStatus) ((LinkedHashMap)tpsResponse.getResponse()).get("status");

        if("00".equals(status.getKode())){
            return (Map) ((LinkedHashMap) tpsResponse.getResponse()).get("data1");
        }

        throw new TpsServiceRutineException(ExceptionTpsServiceRutineMessageCreator.execute(status));
    }
}

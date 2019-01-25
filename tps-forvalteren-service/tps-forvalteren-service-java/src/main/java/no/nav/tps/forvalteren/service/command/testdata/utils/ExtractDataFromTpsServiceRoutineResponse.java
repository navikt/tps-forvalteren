package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;

public final class ExtractDataFromTpsServiceRoutineResponse {

    private ExtractDataFromTpsServiceRoutineResponse() {

    }

    public static Set<String> trekkUtIdenterMedStatusIkkeFunnetFraResponse(TpsServiceRoutineResponse tpsResponse) {

        Map responseMap = (Map) tpsResponse.getResponse();
        int antallIdenter = (int) responseMap.get("antallTotalt");

        Set<String> identer = new HashSet(antallIdenter);

        for (int i = 1; i < antallIdenter + 1; i++) {
            Map data = (Map) responseMap.get("data" + i);
            identer.add(String.valueOf(data.get("fnr")));
        }
        
        return identer;
    }
}

package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;

public class ExtractDataFromTpsServiceRoutineResponse {
    public static Set<String> trekkUtIdenterMedStatusIkkeFunnetFraResponse(TpsServiceRoutineResponse tpsResponse) {
        LinkedHashMap responseMap = (LinkedHashMap) tpsResponse.getResponse();
        Set<String> identer = new HashSet<>();
        
        int antallIdenter = (int) responseMap.get("antallTotalt");
        
        for (int i = 1; i < antallIdenter + 1; i++) {
            LinkedHashMap data = (LinkedHashMap) responseMap.get("data" + i);
            identer.add(String.valueOf(data.get("fnr")));
        }
        
        return identer;
    }
}

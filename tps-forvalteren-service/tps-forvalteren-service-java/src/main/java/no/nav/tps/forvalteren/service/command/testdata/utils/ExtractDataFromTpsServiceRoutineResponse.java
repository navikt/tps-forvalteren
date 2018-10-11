package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.util.LinkedHashMap;
import java.util.Set;
import com.google.common.collect.Sets;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;

public final class ExtractDataFromTpsServiceRoutineResponse {

    private ExtractDataFromTpsServiceRoutineResponse() {

    }

    public static Set<String> trekkUtIdenterMedStatusIkkeFunnetFraResponse(TpsServiceRoutineResponse tpsResponse) {

        LinkedHashMap responseMap = (LinkedHashMap) tpsResponse.getResponse();
        int antallIdenter = (int) responseMap.get("antallTotalt");

        Set<String> identer = Sets.newLinkedHashSetWithExpectedSize(antallIdenter);

        for (int i = 1; i < antallIdenter + 1; i++) {
            LinkedHashMap data = (LinkedHashMap) responseMap.get("data" + i);
            identer.add(String.valueOf(data.get("fnr")));
        }
        
        return identer;
    }
}

package no.nav.tps.forvalteren.service.command.testdata.utils;

import static java.util.Objects.nonNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.NoArgsConstructor;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;

@NoArgsConstructor
public final class ExtractDataFromTpsServiceRoutineResponse {

    public static Set<String> trekkUtIdenterMedStatusIkkeFunnetFraResponse(TpsServiceRoutineResponse tpsResponse) {

        Map responseMap = (Map) tpsResponse.getResponse();
        int antallIdenter = (int) responseMap.get("antallTotalt");

        Set<String> identer = new HashSet();

        ResponseStatus status = (ResponseStatus) getArtifact(responseMap, "status");
        if (!"08".equals(status.getKode())) {
            for (int i = 1; i < antallIdenter + 1; i++) {
                Map data = (Map) getArtifact(responseMap, "data" + i);

                Map aFnr = (Map) getArtifact(data, "AFnr");
                Map svarStatus;
                if (nonNull(aFnr)) {
                    Map eFnr = (Map) getArtifact(aFnr, "EFnr");
                    svarStatus = (Map) getArtifact(data, "svarStatus");
                    String returStatus = (String) getArtifact(svarStatus, "returStatus");
                    if (!"08".equals(returStatus)) {
                        identer.add(String.valueOf(eFnr.get("fnr")));
                    }
                } else {
                    svarStatus = (Map) getArtifact(data, "svarStatus");
                    String returStatus = (String) getArtifact(svarStatus, "returStatus");
                    if (!"08".equals(returStatus)) {
                        identer.add(String.valueOf(data.get("fnr")));
                    }
                }
            }
        }
        return identer;
    }

    private static Object getArtifact(Map map, String key) {
        return nonNull(map) ? map.get(key) : null;
    }
}

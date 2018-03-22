package no.nav.tps.forvalteren.service.command.testdata.utils;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * @author Jarl Øystein Samseth, Visma Consulting
 */
public class ExtractDataFromTpsServiceRoutineResponse {
	public static Set<String> trekkUtIdenterFraResponse(TpsServiceRoutineResponse tpsResponse) {
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

package no.nav.tps.forvalteren.domain.rs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jarl Øystein Samseth, Visma Consulting
 */
@Getter
@AllArgsConstructor
public class RsTpsStatusPaaIdenterResponse {
	List<TpsStatusPaaIdent> statusPaaIdenter;
	
	public void addEnvToTheseIdents(String env, List<String> idents) {
		List<TpsStatusPaaIdent> tpsStatusPaaIdentList= findTpsStatusPaaIdentObject(idents);
		tpsStatusPaaIdentList.forEach(tpsStatusPaaIdent -> tpsStatusPaaIdent.add(env));
	}
	
	public List<TpsStatusPaaIdent> findTpsStatusPaaIdentObject(List<String> identer) {
		return statusPaaIdenter.stream()
				.filter(tpsStatusPaaIdent -> identer.contains(tpsStatusPaaIdent.getIdent()))
				.collect(Collectors.toList());
	}
}

package no.nav.tps.forvalteren.domain.rs.skd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Jarl Øystein Samseth, Visma Consulting
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsSkdMeldingResponse {
	private Long gruppeid;
	
	List<SendSkdMeldingTilTpsResponse> sendSkdMeldingTilTpsResponsene;
	
}
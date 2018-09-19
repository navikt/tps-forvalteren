package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFeltDefinisjon;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans2;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans2;
import no.nav.tps.forvalteren.service.command.testdata.utils.MapBetweenRsMeldingstypeAndSkdMelding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MapToRsMelding {
	
	private ObjectMapper objectMapper;
	private MapBetweenRsMeldingstypeAndSkdMelding mapBetweenRsMeldingstypeAndSkdMelding;
	private SkdFelterContainerTrans2 skdFelterContainerTrans2;
	
	@Autowired
	public MapToRsMelding(ObjectMapper objectMapper, MapBetweenRsMeldingstypeAndSkdMelding mapBetweenRsMeldingstypeAndSkdMelding,
			SkdFelterContainerTrans2 skdFelterContainerTrans2) {
		this.objectMapper = objectMapper;
		this.mapBetweenRsMeldingstypeAndSkdMelding = mapBetweenRsMeldingstypeAndSkdMelding;
		this.skdFelterContainerTrans2 = skdFelterContainerTrans2;
	}
	
	public RsMeldingstype execute(SkdMelding melding) {
		if (melding instanceof SkdMeldingTrans1) {
			return createT1(((SkdMeldingTrans1) melding));
		} else {
			return createT2(((SkdMeldingTrans2) melding).getSkdMelding()); //TODO getSkdMelding og skdFelterContainerTrans2 er midlertidig løsning inntil mapping og java-representasjon for skdMeldingTrans2 er på plass.
		}
	}
	
	private RsMeldingstype createT1(SkdMeldingTrans1 melding) {
		RsMeldingstype1Felter rsMeldingstype = mapBetweenRsMeldingstypeAndSkdMelding.map(melding);
		rsMeldingstype.setBeskrivelse("IDENT: " + rsMeldingstype.getFodselsdato() + rsMeldingstype.getPersonnummer() + " - AARSAK_KO_DE: " + rsMeldingstype
				.getAarsakskode());
 		return rsMeldingstype;
	}
	
	private RsMeldingstype createT2(String melding) {
		List<SkdFeltDefinisjon> felter = skdFelterContainerTrans2.hentSkdFelter();
		Map<String, String> meldingFelter = new HashMap<>();
		populateMapWithFelter(melding, meldingFelter, felter);
		meldingFelter.put("meldingstype", "t2");
		String beskrivelse = "IDENT: " + meldingFelter.get("fodselsnr") + " - AARSAK_KO_DE: " + meldingFelter.get("aarsakskode");
		meldingFelter.put("beskrivelse", beskrivelse);
		return objectMapper.convertValue(meldingFelter, RsMeldingstype2Felter.class);
	}
	
	private void populateMapWithFelter(String melding, Map<String, String> meldingFelter, List<SkdFeltDefinisjon> felter) {
		for (SkdFeltDefinisjon felt : felter) {
			if (felt.getFraByte() != 0 && felt.getTilByte() != 0) {
				String extractedValue = melding.substring(felt.getFraByte() - 1, felt.getTilByte());
				String trimmedValue = extractedValue.trim();
				meldingFelter.put(felt.getNokkelNavn(), trimmedValue);
			}
		}
	}
	
}
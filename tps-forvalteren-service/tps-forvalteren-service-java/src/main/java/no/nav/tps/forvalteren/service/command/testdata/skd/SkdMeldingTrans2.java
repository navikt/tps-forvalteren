package no.nav.tps.forvalteren.service.command.testdata.skd;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO Gjør om skdmeldingen til en java-representasjon av SkdMeldingTrans2 i likhet med SkdMeldingTrans1
 */
@Getter
@AllArgsConstructor
public class SkdMeldingTrans2 implements SkdMelding {
	private static final int TRANSTYPE_START_POSITION = 25;
	private static final int TRANSTYPE_END_POSITION = 26;
	private String skdMelding; //En midlertidig implementasjon av SkdMeldingTrans2
	
	@Override
	public String toString() {
		return skdMelding;
	}

	public String getFodselsnummer() {
		return skdMelding.substring(46, 57);
	}
	
	@Override
	public String getTranstype() {
		return skdMelding.substring(TRANSTYPE_START_POSITION, TRANSTYPE_END_POSITION);
	}
}

package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.junit.Assert.assertEquals;

import com.google.common.io.Resources;
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.SkdFeltDefinisjoner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Jarl Øystein Samseth, Visma Consulting
 */
@RunWith(Parameterized.class)
public class SkdMeldingTrans1Test {
	@Parameterized.Parameter
	public SkdMeldingTrans1 skdMeldingTrans1 = createDoedsmelding();
	@Parameterized.Parameter(1)
	public String resourceName;
	String expectedSkdMessage;
	
	@Parameterized.Parameters
	public static Collection testparameters() {
		return Arrays.asList(new Object[][]{
				{createDoedsmelding(), "skdmeldinger/doedsmelding.txt"},
				{createVigselsmelding(), "skdmeldinger/vigselsmelding.txt"}});
	}
	
	@Before
	public void setup() throws IOException {
		URL fileUrl = Resources.getResource(resourceName);
		String tempexpectedSkdMessage = Resources.toString(fileUrl, StandardCharsets.UTF_8);
		expectedSkdMessage = tempexpectedSkdMessage.substring(0, tempexpectedSkdMessage.length() - "ENDOFFILE".length()); //IntelliJ trimmer txt-filen. Derfor er det lagt til ekstra tegn på slutten av filen.
	}
	
	@Test
	public void shouldReturnStringSkdMessage() {
		assertEquals(expectedSkdMessage, skdMeldingTrans1.toString());
	}
	
	@Test
	public void tryout() {
		System.out.println(SkdFeltDefinisjoner.getAllFeltDefinisjonerInSortedList()
				.stream()
				.collect(Collectors.summingInt(SkdFeltDefinisjoner::getAntallBytesAvsatt)));
	}
	
	private static SkdMeldingTrans1 createVigselsmelding() {
		return SkdMeldingTrans1.builder()
				.header("000000000TPSF0000044216110                    ")
				.vigselstype("1")
				.ektefelleEkteskapPartnerskapNr("1")
				.regdatoSivilstand("20180404")
				.tildelingskode("0")
				.transtype("1")
				.personnummer("52940")
				.regDato("20180404")
				.familienummer("25041650136")
				.maskintid("142656")
				.sivilstand("6")
				.ektefelleTidligereSivilstand("1")
				.vigselskommune("0301")
				.ektefellePartnerPersonnr("50136")
				.ekteskapPartnerskapNr("1")
				.fodselsdato("271116")
				.maskindato("20180404")
				.aarsakskode("61")
				.ektefellePartnerFoedselsdato("250416")
				.tidligereSivilstand("1")
				.regdatoFamnr("20180404").build();
		
	}
	
	private static SkdMeldingTrans1 createDoedsmelding() {
		return SkdMeldingTrans1.builder()
				.header("000000000TPSF0000044214310                    ")
				.fodselsdato("250416")
				.datoDoed("20180802")
				.statuskode("5")
				.maskindato("20180404")
				.transtype("1")
				.personnummer("50136")
				.regDato("20180802")
				.aarsakskode("43")
				.maskintid("142656").build();
	}
	
}
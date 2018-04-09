package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFeltDefinisjon;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans2;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans2;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MapToRsMeldingTest {
	
	private static final String T2_TRANSTYPE_2 = "2";
	private static final String T2_MELDING_WITH_TRANSTYPE_2 = "1337991614920991117151855" + T2_TRANSTYPE_2;
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private SkdFelterContainerTrans2 skdFelterContainerTrans2;
	@InjectMocks
	private MapToRsMelding mapToRsMelding;
	private String skdEndringsmeldingT1WithAllFieldsSupplied;
	private String skdEndringsmeldingT2WithAllFieldsSupplied;
	
	@Before
	public void setup() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		skdEndringsmeldingT1WithAllFieldsSupplied = FileUtils.fileRead(new File(classLoader.getResource("melding-t1-alle-felter-utfylt.txt")
				.getFile()));
		skdEndringsmeldingT2WithAllFieldsSupplied = FileUtils.fileRead(new File(classLoader.getResource("melding-t2-alle-felter-utfylt.txt")
				.getFile()));
		
		RsMeldingstype1Felter meldingT1 = new RsMeldingstype1Felter();
		RsMeldingstype2Felter meldingT2 = new RsMeldingstype2Felter();
		when(objectMapper.convertValue(any(HashMap.class), eq(RsMeldingstype1Felter.class))).thenReturn(meldingT1);
		when(objectMapper.convertValue(any(HashMap.class), eq(RsMeldingstype2Felter.class))).thenReturn(meldingT2);
		
		List<SkdFeltDefinisjon> felter = new ArrayList<>();
		when(skdFelterContainerTrans2.hentSkdFelter()).thenReturn(felter);
	}
	
	@Test
	public void returnsT1() {
		RsMeldingstype melding = mapToRsMelding.execute(new SkdMeldingTrans1());
		
		assertThat(melding, instanceOf(RsMeldingstype1Felter.class));
	}
	
	@Test
	public void returnsT1WhenCalledWithSkdEndringsmeldingAllFieldsSupplied() {
		RsMeldingstype melding = mapToRsMelding.execute(SkdMeldingTrans1.unmarshal(skdEndringsmeldingT1WithAllFieldsSupplied));
		
		assertThat(melding, instanceOf(RsMeldingstype1Felter.class));
	}
	
	@Test
	public void checkThatAllFielsGetInitializedT1() {
		
		RsMeldingstype melding = mapToRsMelding.execute(SkdMeldingTrans1.unmarshal(skdEndringsmeldingT1WithAllFieldsSupplied));
		
		assertThat(melding, instanceOf(RsMeldingstype1Felter.class));
		for (Field f : melding.getClass().getDeclaredFields()) {
			assertThat(f, is(not(nullValue())));
		}
	}
	
	@Test
	public void checkThatAllFielsGetInitializedT2() {
		when(skdFelterContainerTrans2.hentSkdFelter()).thenCallRealMethod();
		
		RsMeldingstype melding = mapToRsMelding.execute(new SkdMeldingTrans2(skdEndringsmeldingT2WithAllFieldsSupplied));
		
		assertThat(melding, instanceOf(RsMeldingstype2Felter.class));
		for (Field f : melding.getClass().getDeclaredFields()) {
			assertThat(f, is(not(nullValue())));
		}
	}
	
	@Test
	public void SkdMelding2ReturnsT2() {
		RsMeldingstype melding = mapToRsMelding.execute(new SkdMeldingTrans2(T2_MELDING_WITH_TRANSTYPE_2));
		
		assertThat(melding, instanceOf(RsMeldingstype2Felter.class));
	}
	
	@Test
	public void shouldReturnT2WhenCalledWithSkdEndringsmeldingAllFieldsSupplied() {
		when(skdFelterContainerTrans2.hentSkdFelter()).thenCallRealMethod();
		
		RsMeldingstype melding = mapToRsMelding.execute(new SkdMeldingTrans2(skdEndringsmeldingT2WithAllFieldsSupplied));
		
		assertThat(melding, instanceOf(RsMeldingstype2Felter.class));
	}
	
}
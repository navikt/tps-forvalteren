package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFeltDefinisjon;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans2;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans2;
import no.nav.tps.forvalteren.service.command.testdata.utils.MapBetweenRsMeldingstypeAndSkdMelding;

@RunWith(MockitoJUnitRunner.class)
public class MapToRsMeldingTest {
	
	private static final String T2_TRANSTYPE_2 = "2";
	private static final String T2_MELDING_WITH_TRANSTYPE_2 = "1337991614920991117151855" + T2_TRANSTYPE_2;
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	private ObjectMapper objectMapper = mock(ObjectMapper.class);
	private SkdFelterContainerTrans2 skdFelterContainerTrans2 = mock(SkdFelterContainerTrans2.class);
	
	private MapBetweenRsMeldingstypeAndSkdMelding mapBetweenRsMeldingstypeAndSkdMelding = new MapBetweenRsMeldingstypeAndSkdMelding();
	private MapToRsMelding mapToRsMelding = new MapToRsMelding(objectMapper,mapBetweenRsMeldingstypeAndSkdMelding,skdFelterContainerTrans2);
	private String skdEndringsmeldingT1WithAllFieldsSupplied;
	private String skdEndringsmeldingT2WithAllFieldsSupplied;
	
	@Before
	public void setup() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		skdEndringsmeldingT1WithAllFieldsSupplied = FileUtils.fileRead(new File(classLoader.getResource("melding-t1-alle-felter-utfylt.txt")
				.getFile()));
		skdEndringsmeldingT2WithAllFieldsSupplied = FileUtils.fileRead(new File(classLoader.getResource("melding-t2-alle-felter-utfylt.txt")
				.getFile()));
		
		RsMeldingstype2Felter meldingT2 = new RsMeldingstype2Felter();
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
	public void returnsT1WhenCalledWithSkdEndringsmeldingAllFieldsSupplied() throws InvocationTargetException, IllegalAccessException {
		RsMeldingstype melding = mapToRsMelding.execute(SkdMeldingTrans1.unmarshal(skdEndringsmeldingT1WithAllFieldsSupplied));
		
		assertThat(melding, instanceOf(RsMeldingstype1Felter.class));
		assertNoNullFields(melding);
	}
	
	private void assertNoNullFields(RsMeldingstype melding) throws InvocationTargetException, IllegalAccessException {
		for (Method f: melding.getClass().getMethods()) {
			if (f.getName().length() > 2 && "get".equals(f.getName().substring(0, 3)) && !f.getName().equals("getId")) {
				Assert.assertNotNull(f.getName(), f.invoke(melding));
			}
		}
	}
	
	@Test
	public void checkThatAllFielsGetInitializedT2() throws InvocationTargetException, IllegalAccessException {
		when(skdFelterContainerTrans2.hentSkdFelter()).thenCallRealMethod();
		
		RsMeldingstype melding = mapToRsMelding.execute(new SkdMeldingTrans2(skdEndringsmeldingT2WithAllFieldsSupplied));
		
		assertThat(melding, instanceOf(RsMeldingstype2Felter.class));
//		assertNoNullFields(melding); //TODO introdusér når objectmapper byttes ut med mapperfacade
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
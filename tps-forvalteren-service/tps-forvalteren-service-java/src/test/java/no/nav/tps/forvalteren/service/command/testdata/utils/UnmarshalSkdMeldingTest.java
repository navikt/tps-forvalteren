package no.nav.tps.forvalteren.service.command.testdata.utils;

import static no.nav.tps.forvalteren.common.message.MessageConstants.SKD_ILLEGAL_MELDINGSTYPE;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.service.command.exceptions.IllegalMeldingstypeException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans2;

@RunWith(MockitoJUnitRunner.class)
public class UnmarshalSkdMeldingTest {
	private static final String ILLEGAL_TRANSTYPE = "0";
	private static final String T1 = "1";
	private static final String T2_TRANSTYPE_2 = "2";
	private static final String T2_TRANSTYPE_3 = "3";
	private static final String T2_TRANSTYPE_4 = "4";
	private static final String MELDING_WITH_ILLEGAL_TRANSTYPE = "1337991614920991117151855" + ILLEGAL_TRANSTYPE;
	private static final String T2_MELDING_WITH_TRANSTYPE_2 = "1337991614920991117151855" + T2_TRANSTYPE_2;
	private static final String T2_MELDING_WITH_TRANSTYPE_3 = "1337991614920991117151855" + T2_TRANSTYPE_3;
	private static final String T2_MELDING_WITH_TRANSTYPE_4 = "1337991614920991117151855" + T2_TRANSTYPE_4;
	private static StringBuilder stringRestOfSkdmeldingTrans1 = new StringBuilder(1500);
	static {		stringRestOfSkdmeldingTrans1.setLength(1500);	}
	private static final String T1_MELDING_WITH_TRANSTYPE_1 = "1337992055120990630073403" + T1 + stringRestOfSkdmeldingTrans1.toString();

	@InjectMocks
	UnmarshalSkdMelding unmarshalSkdMelding;
	@Mock
	private MessageProvider messageProvider;
	
	@Test(expected = IllegalMeldingstypeException.class)
	public void throwsIllegalMeldingstypeException() {
		unmarshalSkdMelding.unmarshalMeldingUtenHeader(MELDING_WITH_ILLEGAL_TRANSTYPE);
		verify(messageProvider).get(SKD_ILLEGAL_MELDINGSTYPE, ILLEGAL_TRANSTYPE);
	}
	
	@Test
	public void shouldUnmarshalToSkdMeldingTrans1() {
		SkdMelding skdMelding = unmarshalSkdMelding.unmarshalMeldingUtenHeader(T1_MELDING_WITH_TRANSTYPE_1);
		assertThat(skdMelding, instanceOf(SkdMeldingTrans1.class));
	}
	
	@Test
	public void shouldUnmarshalToSkdMeldingTrans2() {
		SkdMelding skdMelding = unmarshalSkdMelding.unmarshalMeldingUtenHeader(T2_MELDING_WITH_TRANSTYPE_2);
		assertThat(skdMelding, instanceOf(SkdMeldingTrans2.class));
	}
	
	@Test
	public void transtype3ReturnsT2() {
		SkdMelding skdMelding = unmarshalSkdMelding.unmarshalMeldingUtenHeader(T2_MELDING_WITH_TRANSTYPE_3);
		assertThat(skdMelding, instanceOf(SkdMeldingTrans2.class));
	}
	
	@Test
	public void transtype4ReturnsT2() {
		SkdMelding skdMelding = unmarshalSkdMelding.unmarshalMeldingUtenHeader(T2_MELDING_WITH_TRANSTYPE_4);
		assertThat(skdMelding, instanceOf(SkdMeldingTrans2.class));
	}
	
}
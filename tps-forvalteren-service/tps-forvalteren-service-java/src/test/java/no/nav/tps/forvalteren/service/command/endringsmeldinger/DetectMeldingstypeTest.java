package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ILLEGAL_MELDINGSTYPE;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
import no.nav.tps.forvalteren.service.command.exceptions.IllegalMeldingstypeException;

@RunWith(MockitoJUnitRunner.class)
public class DetectMeldingstypeTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private MessageProvider messageProvider;

    @InjectMocks
    private DetectMeldingstype detectMeldingstype;

    private static final String T1 = "1";
    private static final String T2_TRANSTYPE_2 = "2";
    private static final String T2_TRANSTYPE_3 = "3";
    private static final String T2_TRANSTYPE_4 = "4";
    private static final String ILLEGAL_TRANSTYPE = "0";

    private static final String T1_MELDING_WITH_TRANSTYPE_1 = "1337992055120990630073403" + T1;
    private static final String T2_MELDING_WITH_TRANSTYPE_2 = "1337991614920991117151855" + T2_TRANSTYPE_2;
    private static final String T2_MELDING_WITH_TRANSTYPE_3 = "1337991614920991117151855" + T2_TRANSTYPE_3;
    private static final String T2_MELDING_WITH_TRANSTYPE_4 = "1337991614920991117151855" + T2_TRANSTYPE_4;
    private static final String MELDING_WITH_ILLEGAL_TRANSTYPE = "1337991614920991117151855" + ILLEGAL_TRANSTYPE;

    @Test
    public void returnsT1() {
        RsMeldingstype melding = detectMeldingstype.execute(T1_MELDING_WITH_TRANSTYPE_1);

        assertThat(melding, instanceOf(RsMeldingstype1Felter.class));
    }

    @Test
    public void meldingstype2ReturnsT2() {
        RsMeldingstype melding = detectMeldingstype.execute(T2_MELDING_WITH_TRANSTYPE_2);

        assertThat(melding, instanceOf(RsMeldingstype2Felter.class));
    }

    @Test
    public void meldingstype3ReturnsT2() {
        RsMeldingstype melding = detectMeldingstype.execute(T2_MELDING_WITH_TRANSTYPE_3);

        assertThat(melding, instanceOf(RsMeldingstype2Felter.class));
    }

    @Test
    public void meldingstype4ReturnsT2() {
        RsMeldingstype melding = detectMeldingstype.execute(T2_MELDING_WITH_TRANSTYPE_4);

        assertThat(melding, instanceOf(RsMeldingstype2Felter.class));
    }

    @Test
    public void throwsIllegalMeldingstypeException() {
        expectedException.expect(IllegalMeldingstypeException.class);
        detectMeldingstype.execute(MELDING_WITH_ILLEGAL_TRANSTYPE);
        verify(messageProvider).get(SKD_ILLEGAL_MELDINGSTYPE, ILLEGAL_TRANSTYPE);
    }

}
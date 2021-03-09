package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.message.MessageConstants.SKD_ILLEGAL_MELDINGSTYPE;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
import no.nav.tps.forvalteren.service.command.exceptions.IllegalMeldingstypeException;

@RunWith(MockitoJUnitRunner.class)
public class GetRsMeldingstypeFromTypeTextTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private MessageProvider messageProvider;

    @InjectMocks
    private GetRsMeldingstypeFromTypeText GetRsMeldingstypeFromTypeText;

    private static final String MELDINGSTYPE_T1 = "t1";
    private static final String MELDINGSTYPE_T2 = "t2";
    private static final String MELDINGSTYPE_ILLEGAL = "t1337";

    @Test
    public void returnsMeldingstype1() {
        RsMeldingstype result = GetRsMeldingstypeFromTypeText.execute(MELDINGSTYPE_T1);
        assertThat(result, instanceOf(RsMeldingstype1Felter.class));
    }

    @Test
    public void returnsMeldingstype2() {
        RsMeldingstype result = GetRsMeldingstypeFromTypeText.execute(MELDINGSTYPE_T2);
        assertThat(result, instanceOf(RsMeldingstype2Felter.class));
    }

    @Test
    public void throwsIllegalMeldingstypeException() {
        doThrow(IllegalMeldingstypeException.class).when(messageProvider).get(SKD_ILLEGAL_MELDINGSTYPE, MELDINGSTYPE_ILLEGAL);

        expectedException.expect(IllegalMeldingstypeException.class);

        GetRsMeldingstypeFromTypeText.execute(MELDINGSTYPE_ILLEGAL);
        verify(messageProvider).get(SKD_ILLEGAL_MELDINGSTYPE, MELDINGSTYPE_ILLEGAL);
    }

}
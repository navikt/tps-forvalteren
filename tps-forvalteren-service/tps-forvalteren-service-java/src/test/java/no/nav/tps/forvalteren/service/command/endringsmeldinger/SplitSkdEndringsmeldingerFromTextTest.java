package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_ILLEGAL_LENGTH;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.codehaus.plexus.util.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingIllegalLengthException;

@RunWith(MockitoJUnitRunner.class)
public class SplitSkdEndringsmeldingerFromTextTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private MessageProvider messageProvider;

    @InjectMocks
    private SplitSkdEndringsmeldingerFromText splitSkdEndringsmeldingerFromText;

    private static final int ILLLEGAL_LENGTH = 1337;

    @Before
    public void setup() {
        when(messageProvider.get(SKD_ENDRINGSMELDING_ILLEGAL_LENGTH, ILLLEGAL_LENGTH)).thenReturn("illegal length");
    }

    @Test
    public void returnsOneMelding() {
        String oneMelding = StringUtils.repeat(" ", 1500);

        List<String> result = splitSkdEndringsmeldingerFromText.execute(oneMelding);

        assertThat(result, hasSize(1));
    }

    @Test
    public void returnsTenMeldinger() {
        String tenMeldinger = StringUtils.repeat(" ", 15000);

        List<String> result = splitSkdEndringsmeldingerFromText.execute(tenMeldinger);

        assertThat(result, hasSize(10));
    }

    @Test
    public void splitsMeldingerCorrectly() {
        String melding1 = StringUtils.repeat("1", 1500);
        String melding2 = StringUtils.repeat("2", 1500);
        String melding3 = StringUtils.repeat("3", 1500);
        String threeMeldinger = melding1 + melding2 + melding3;

        List<String> result = splitSkdEndringsmeldingerFromText.execute(threeMeldinger);

        assertThat(result, hasSize(3));
        assertThat(result.get(0), equalTo(melding1));
        assertThat(result.get(1), equalTo(melding2));
        assertThat(result.get(2), equalTo(melding3));
    }

    @Test
    public void throwsSkdEndringsmeldingIllegalLengthException() {
        String meldinger = StringUtils.repeat(" ", ILLLEGAL_LENGTH);

        expectedException.expect(SkdEndringsmeldingIllegalLengthException.class);
        expectedException.expectMessage("illegal length");

        splitSkdEndringsmeldingerFromText.execute(meldinger);

        verify(messageProvider).get(SKD_ENDRINGSMELDING_ILLEGAL_LENGTH, ILLLEGAL_LENGTH);
    }

}
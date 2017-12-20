package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingJsonToObjectException;

@RunWith(MockitoJUnitRunner.class)
public class ConvertJsonToRsMeldingstypeTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private ConvertJsonToRsMeldingstype convertJsonToRsMeldingstype;

    @Mock
    private SkdEndringsmelding melding;

    private static final String MELDING_TEXT = "melding";

    @Before
    public void setup() throws IOException {
        when(melding.getEndringsmelding()).thenReturn(MELDING_TEXT);
        when(mapper.readValue(melding.getEndringsmelding(), RsMeldingstype.class)).thenReturn(new RsMeldingstype1Felter());
    }

    @Test
    public void returnsMeldingstype() throws IOException {
        RsMeldingstype result = convertJsonToRsMeldingstype.execute(melding);

        verify(mapper).readValue(MELDING_TEXT, RsMeldingstype.class);

        assertThat(result, instanceOf(RsMeldingstype1Felter.class));
    }

    @Test
    public void throwsCorrectException() throws IOException {
        doThrow(SkdEndringsmeldingJsonToObjectException.class).when(mapper).readValue(MELDING_TEXT, RsMeldingstype.class);

        expectedException.expect(SkdEndringsmeldingJsonToObjectException.class);

        convertJsonToRsMeldingstype.execute(melding);
    }

}
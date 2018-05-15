package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans2;
import no.nav.tps.forvalteren.service.command.testdata.utils.UnmarshalSkdMelding;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsRawMeldinger;

@RunWith(MockitoJUnitRunner.class)
public class CreateAndSaveSkdEndringsmeldingerFromTextTest {

    @Mock
    private SplitSkdEndringsmeldingerFromText splitSkdEndringsmeldingerFromText;

    @Mock
    private SaveSkdEndringsmeldingerFromText saveSkdEndringsmeldingerFromText;

    @Mock
    private CreateMeldingWithMeldingstype createMeldingWithMeldingstype;
    @Mock
    private UnmarshalSkdMelding unmarshalSkdMelding;

    @InjectMocks
    private CreateAndSaveSkdEndringsmeldingerFromText createAndSaveSkdEndringsmeldingerFromText;
    
    @Mock
    private RsRawMeldinger rawMeldinger;
    
    SkdMeldingTrans2 melding = new SkdMeldingTrans2("some message");
    private List<SkdMelding> meldinger = Arrays.asList(melding);
    
    private List<String > meldingerString = Arrays.asList(melding.getSkdMelding());
    
    @Mock
    private List<RsMeldingstype> rsMeldingstyper;
    
    private static final Long GRUPPE_ID = 1337L;
    
    @Before
    public void setup() {
        when(splitSkdEndringsmeldingerFromText.execute(rawMeldinger.getRaw())).thenReturn(meldingerString);
        when(createMeldingWithMeldingstype.execute(meldinger)).thenReturn(rsMeldingstyper);
        when(unmarshalSkdMelding.unmarshalMeldingUtenHeader(anyString())).thenReturn(melding);
    }
    
    @Test
    public void checkThatAllServicesGetsCalled() {
        createAndSaveSkdEndringsmeldingerFromText.execute(GRUPPE_ID, rawMeldinger);
        
        verify(splitSkdEndringsmeldingerFromText).execute(rawMeldinger.getRaw());
        verify(createMeldingWithMeldingstype).execute(meldinger);
        verify(saveSkdEndringsmeldingerFromText).execute(rsMeldingstyper, GRUPPE_ID);
    }
}
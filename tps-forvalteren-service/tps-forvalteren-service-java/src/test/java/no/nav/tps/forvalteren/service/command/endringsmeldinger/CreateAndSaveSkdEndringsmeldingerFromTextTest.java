package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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

    @InjectMocks
    private CreateAndSaveSkdEndringsmeldingerFromText createAndSaveSkdEndringsmeldingerFromText;
    
    @Mock
    private RsRawMeldinger rawMeldinger;
    
    @Mock
    private List<String> meldinger;
    
    @Mock
    private List<RsMeldingstype> rsMeldingstyper;
    
    private static final Long GRUPPE_ID = 1337L;
    
    @Before
    public void setup() {
        when(splitSkdEndringsmeldingerFromText.execute(rawMeldinger.getRaw())).thenReturn(meldinger);
        when(createMeldingWithMeldingstype.execute(meldinger)).thenReturn(rsMeldingstyper);
    }
    
    @Test
    public void checkThatAllServicesGetsCalled() {
        createAndSaveSkdEndringsmeldingerFromText.execute(GRUPPE_ID, rawMeldinger);
        
        verify(splitSkdEndringsmeldingerFromText).execute(rawMeldinger.getRaw());
        verify(createMeldingWithMeldingstype).execute(meldinger);
        verify(saveSkdEndringsmeldingerFromText).execute(rsMeldingstyper, GRUPPE_ID);
    }
}
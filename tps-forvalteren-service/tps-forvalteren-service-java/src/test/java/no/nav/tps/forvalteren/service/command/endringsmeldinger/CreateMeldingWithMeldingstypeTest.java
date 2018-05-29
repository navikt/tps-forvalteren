package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.mockito.Mockito.verify;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CreateMeldingWithMeldingstypeTest {

    @Mock
    private MapToRsMelding mapToRsMelding;

    @InjectMocks
    private CreateMeldingWithMeldingstype createMeldingWithMeldingstype;
    
    @Test
    public void verifyServiceCall() {
        List<SkdMelding> meldinger = Arrays.asList(new SkdMeldingTrans2("melding"));
        
        createMeldingWithMeldingstype.execute(meldinger);
        
        verify(mapToRsMelding).execute(meldinger.get(0));
    }

    @Test
    public void verifyMultipleServiceCall() {
        List<SkdMelding> meldinger = Arrays.asList(new SkdMeldingTrans2("melding"), new SkdMeldingTrans2("melding2"), new SkdMeldingTrans2("melding3"));

        createMeldingWithMeldingstype.execute(meldinger);

        verify(mapToRsMelding).execute(meldinger.get(0));
        verify(mapToRsMelding).execute(meldinger.get(1));
        verify(mapToRsMelding).execute(meldinger.get(2));
    }

}
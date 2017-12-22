package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreateMeldingWithMeldingstypeTest {

    @Mock
    private DetectMeldingstype detectMeldingstype;

    @InjectMocks
    private CreateMeldingWithMeldingstype createMeldingWithMeldingstype;
    
    @Test
    public void verifyServiceCall() {
        List<String> meldinger = Arrays.asList("melding");
        
        createMeldingWithMeldingstype.execute(meldinger);
        
        verify(detectMeldingstype).execute(meldinger.get(0));
    }

    @Test
    public void verifyMultipleServiceCall() {
        List<String> meldinger = Arrays.asList("melding", "melding2", "melding3");

        createMeldingWithMeldingstype.execute(meldinger);

        verify(detectMeldingstype).execute(meldinger.get(0));
        verify(detectMeldingstype).execute(meldinger.get(1));
        verify(detectMeldingstype).execute(meldinger.get(2));
    }

}
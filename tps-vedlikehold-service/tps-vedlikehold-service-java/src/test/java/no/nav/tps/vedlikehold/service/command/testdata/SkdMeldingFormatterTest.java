package no.nav.tps.vedlikehold.service.command.testdata;

import no.nav.tps.vedlikehold.domain.service.command.tps.testdata.SkdFeltDefinisjon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Peter Fløgstad on 05.01.2017.
 */

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration()
@RunWith(MockitoJUnitRunner.class)
public class SkdMeldingFormatterTest {

    public static final String T1_FODSELSNR_DNR = "T1-FODSELSDATO";
    public static final String T1_PERSONNUMMER = "T1-PERSONNUMMER";
    public static final String T1_SLEKTSNAVN = "T1-SLEKTSNAVN";
    public static final String T1_FORNAVN = "T1-FORNAVN";

    @InjectMocks
    private SkdMeldingFormatter skdMeldingFormatter;

    @Test
    public void name() throws Exception {
        Map<String,String> verdier = new HashMap<>();
        verdier.put(T1_FODSELSNR_DNR,"070188");
        verdier.put(T1_PERSONNUMMER, "33152");
        verdier.put(T1_SLEKTSNAVN, "Flogstad");
        verdier.put(T1_FORNAVN, "Peter");

        System.out.println("result = " + skdMeldingFormatter.convertToSkdMelding(verdier));
    }


}

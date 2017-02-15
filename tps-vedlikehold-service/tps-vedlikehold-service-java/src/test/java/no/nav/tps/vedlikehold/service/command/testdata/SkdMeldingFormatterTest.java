package no.nav.tps.vedlikehold.service.command.testdata;

import no.nav.tps.vedlikehold.domain.service.tps.testdata.SkdFeltDefinisjon;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Peter Fløgstad on 05.01.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class SkdMeldingFormatterTest {

    public static final String T1_FODSELSDATO = "T1-FODSELSDATO";
    public static final String T1_PERSONNUMMER = "T1-PERSONNUMMER";
    public static final String T1_SLEKTSNAVN = "T1-SLEKTSNAVN";
    public static final String T1_FORNAVN = "T1-FORNAVN";

    private static final String whitespace25stk = "                         ";
    private static final String whitespace50stk = whitespace25stk+whitespace25stk;

    @Mock
    SkdFelterContainer skdFelterContainer;

    @InjectMocks
    private SkdMeldingFormatter skdMeldingFormatter;

    @Before
    public void setup(){
        ArrayList<SkdFeltDefinisjon> SkdFelter = new ArrayList<>();

        SkdFelter.add(new SkdFeltDefinisjon("T1-FODSELSDATO", "000000", 1,6,1,6));
        SkdFelter.add(new SkdFeltDefinisjon("T1-PERSONNUMMER", "00000", 2, 5,7,11));
        SkdFelter.add(new SkdFeltDefinisjon("T1-MASKINDATO", "00000000", 3, 8,12,19));
        SkdFelter.add(new SkdFeltDefinisjon("T1-MASKINTID", "000000", 4, 6,20,25));
        SkdFelter.add(new SkdFeltDefinisjon("T1-TRANSTYPE", "0", 5, 1,26,26));
        SkdFelter.add(new SkdFeltDefinisjon("T1-AARSAKSKODE", "00", 6, 2,27,28));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REG-DATO", "00000000", 7, 8,29,36));
        SkdFelter.add(new SkdFeltDefinisjon("T1-STATUSKODE", " ", 8, 1,37,37));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DATO-DOED", "00000000", 9, 8,38,45));
        SkdFelter.add(new SkdFeltDefinisjon("T1-SLEKTSNAVN", whitespace50stk, 10,50,46,95));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FORNAVN", whitespace50stk, 11,50,96,145));
        SkdFelter.add(new SkdFeltDefinisjon("T1-MELLOMNAVN", whitespace50stk, 12,50,146,195));

        when(skdFelterContainer.hentSkdFelter()).thenReturn(SkdFelter);
    }

    @Test
    public void buildSkdMeldingTest() throws Exception {
        Map<String,String> skdInput = new HashMap<>();
        skdInput.put(T1_FODSELSDATO,"070188");
        skdInput.put(T1_PERSONNUMMER, "33152");
        skdInput.put(T1_SLEKTSNAVN, "Flogstad");
        skdInput.put(T1_FORNAVN, "Peter");

        String skdMelding = skdMeldingFormatter.convertToSkdMeldingInnhold(skdInput);
        String personnummer = skdMelding.substring(6,11);
        String slektsnavn = skdMelding.substring(45,95);
        String fornavn = skdMelding.substring(95,145);
        String fodselsdato = skdMelding.substring(0,6);
        assertEquals(skdInput.get(T1_PERSONNUMMER), personnummer);
        assertEquals(skdInput.get(T1_FODSELSDATO), fodselsdato);
        assertEquals(skdInput.get(T1_SLEKTSNAVN)+"                                          ", slektsnavn);
        assertEquals(skdInput.get(T1_FORNAVN)+"                                             ", fornavn);
    }
}

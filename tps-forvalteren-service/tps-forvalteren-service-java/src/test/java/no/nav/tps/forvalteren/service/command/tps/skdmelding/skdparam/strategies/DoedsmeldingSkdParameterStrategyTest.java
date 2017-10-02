package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.DoedsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.InnvandringSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;

@RunWith(MockitoJUnitRunner.class)
public class DoedsmeldingSkdParameterStrategyTest {

    private static final String FNR = "01234567890";
    private static final LocalDateTime REGDATO = LocalDateTime.of(2017, 9, 29, 15, 38, 0);
    private static final LocalDateTime DOEDSDATO = LocalDateTime.of(2017, 10, 2, 8, 0, 0);

    private static final String REGDATO_DATE_STRING = "20170929";
    private static final String REGDATO_TIME_STRING = "153800";
    private static final String DOEDSDATO_DATE_STRING = "20171002";

    private static final String AARSAKSKODE_KEY = "T1-AARSAKSKODE";
    private static final String TRANSTYPE_KEY = "T1-TRANSTYPE";
    private static final String STATUSKODE_KEY = "T1-STATUSKODE";

    private static final String AARSAKSKODE_FOR_DOEDSMELDING = "43";
    private static final String TRANSTYPE_FOR_DOEDSMELDING = "1";
    private static final String STATUSKODE_FOR_DOEDSMELDING = "5";

    private Person aPerson;

    private DoedsmeldingSkdParameterStrategy doedsmeldingSkdParameterStrategy;

    @Before
    public void setup() {
        doedsmeldingSkdParameterStrategy = new DoedsmeldingSkdParameterStrategy();

        aPerson = mock(Person.class);

        when(aPerson.getIdent()).thenReturn(FNR);
        when(aPerson.getRegdato()).thenReturn(REGDATO);
        when(aPerson.getDoedsdato()).thenReturn(DOEDSDATO);
    }

    @Test
    public void isSupportedTest() {
        SkdParametersCreator correctObject = mock(DoedsmeldingSkdParametere.class);
        SkdParametersCreator incorrectObject = mock(InnvandringSkdParametere.class);

        assertTrue(doedsmeldingSkdParameterStrategy.isSupported(correctObject));
        assertFalse(doedsmeldingSkdParameterStrategy.isSupported(incorrectObject));
    }

    @Test
    public void createCorrectSkdParameterMapFromPerson() {
        Map<String, String> result = doedsmeldingSkdParameterStrategy.execute(aPerson);

        assertEquals(result.get(SkdConstants.FODSELSDATO), FNR.substring(0, 6));
        assertEquals(result.get(SkdConstants.PERSONNUMMER), FNR.substring(6, 11));
        assertEquals(result.get(SkdConstants.MASKINTID), REGDATO_TIME_STRING);
        assertEquals(result.get(SkdConstants.MASKINDATO), REGDATO_DATE_STRING);
        assertEquals(result.get(SkdConstants.REG_DATO), DOEDSDATO_DATE_STRING);
        assertEquals(result.get(SkdConstants.DOEDSDATO), DOEDSDATO_DATE_STRING);
        assertEquals(result.get(AARSAKSKODE_KEY), AARSAKSKODE_FOR_DOEDSMELDING);
        assertEquals(result.get(TRANSTYPE_KEY), TRANSTYPE_FOR_DOEDSMELDING);
        assertEquals(result.get(STATUSKODE_KEY), STATUSKODE_FOR_DOEDSMELDING);
    }
}

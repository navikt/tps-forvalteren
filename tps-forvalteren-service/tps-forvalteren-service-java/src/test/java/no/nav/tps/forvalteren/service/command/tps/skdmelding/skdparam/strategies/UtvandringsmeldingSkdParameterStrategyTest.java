package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.DoedsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.UtvandringSkdParametere;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UtvandringsmeldingSkdParameterStrategyTest {

    @InjectMocks
    private UtvandringsSkdParameterStrategy utvandringsSkdParameterStrategy;

    private static final String FNR = "01010133344";
    private static final String UTVANDRET_TIL_LAND = "057";
    private static final LocalDateTime UTVANDRET_DATO = LocalDateTime.of(2000, 1, 1, 12, 00, 00);
    private static final LocalDateTime FLYTTE_DATO = LocalDateTime.of(2010, 2, 2, 14, 00, 00);

    private static final String UTVANDRET_DATO_STRING = "20000101";
    private static final String FLYTTE_DATO_STRING = "20100202";

    private Person person;

    @Before
    public void setup() {
        person = new Person();
        person.setIdent(FNR);
        person.setRegdato(LocalDateTime.now());
        person.setUtvandretTilLand(UTVANDRET_TIL_LAND);
        person.setUtvandretTilLandFlyttedato(UTVANDRET_DATO);
        person.setUtvandretTilLandRegdato(FLYTTE_DATO);

    }

    @Test
    public void isSupportedTest() {
        SkdParametersCreator correctObject = mock(UtvandringSkdParametere.class);
        SkdParametersCreator incorrectObject = mock(DoedsmeldingSkdParametere.class);

        assertThat(utvandringsSkdParameterStrategy.isSupported(correctObject), is(true));
        assertThat(utvandringsSkdParameterStrategy.isSupported(incorrectObject), is(false));
    }

    @Test
    public void createCorrectSkdParameterMapFromPerson() {
        SkdMeldingTrans1 resultat = utvandringsSkdParameterStrategy.execute(person);

        assertThat(resultat.getFodselsdato(), is(FNR.substring(0, 6)));
        assertThat(resultat.getPersonnummer(), is(FNR.substring(6, 11)));
        assertThat(resultat.getUtvandretTilLand(), is(UTVANDRET_TIL_LAND));
        assertThat(resultat.getTilLandRegdato(), is(UTVANDRET_DATO_STRING));
        assertThat(resultat.getTilLandFlyttedato(), is(FLYTTE_DATO_STRING));

    }
}

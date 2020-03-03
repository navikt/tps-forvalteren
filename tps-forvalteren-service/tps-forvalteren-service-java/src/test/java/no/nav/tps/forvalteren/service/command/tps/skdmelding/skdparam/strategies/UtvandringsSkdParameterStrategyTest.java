package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import static java.util.Collections.singletonList;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.UTVANDRET;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.DoedsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.UtvandringSkdParametere;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.SetAdresseService;

@RunWith(MockitoJUnitRunner.class)
public class UtvandringsSkdParameterStrategyTest {

    @Mock
    private LandkodeEncoder landkodeEncoder;

    @Mock
    private SetAdresseService setAdresseService;

    @InjectMocks
    private UtvandringsSkdParameterStrategy utvandringsSkdParameterStrategy;

    private static final String FNR = "01010133344";
    private static final String UTVANDRET_LANDKODE = "500";
    private static final String UTVANDRET_TIL_LAND = "CYP";
    private static final LocalDateTime UTVANDRET_DATO = LocalDateTime.of(2000, 1, 1, 12, 00, 00);
    private static final LocalDateTime FLYTTE_DATO = LocalDateTime.of(2010, 2, 2,0,0);

    private static final String UTVANDRET_DATO_STRING = "20000101";
    private static final String FLYTTE_DATO_STRING = "20100202";

    private Person person;

    @Before
    public void setup() {
        person = new Person();
        person.setIdent(FNR);
        person.setRegdato(LocalDateTime.now());
        person.setInnvandretUtvandret(singletonList(
                InnvandretUtvandret.builder()
                .innutvandret(UTVANDRET)
                .landkode(UTVANDRET_LANDKODE)
                .flyttedato(FLYTTE_DATO)
                .build()
        ));
    }

    @Test
    public void isSupportedTest() {
        SkdParametersCreator correctObject = mock(UtvandringSkdParametere.class);
        SkdParametersCreator incorrectObject = mock(DoedsmeldingSkdParametere.class);

        assertThat(utvandringsSkdParameterStrategy.isSupported(correctObject), is(true));
        assertThat(utvandringsSkdParameterStrategy.isSupported(incorrectObject), is(false));
    }

    @Ignore
    @Test
    public void createCorrectSkdParameterMapFromPerson() {

        when(landkodeEncoder.encode(UTVANDRET_LANDKODE)).thenReturn(UTVANDRET_TIL_LAND);

        SkdMeldingTrans1 resultat = utvandringsSkdParameterStrategy.execute(person);

        assertThat(resultat.getFodselsdato(), is(FNR.substring(0, 6)));
        assertThat(resultat.getPersonnummer(), is(FNR.substring(6, 11)));
        assertThat(resultat.getUtvandretTilLand(), is(UTVANDRET_TIL_LAND));
        assertThat(resultat.getTilLandRegdato(), is(UTVANDRET_DATO_STRING));
        assertThat(resultat.getTilLandFlyttedato(), is(FLYTTE_DATO_STRING));

    }
}

package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.IdentType;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.DoedsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.InnvandringCreateSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;

@RunWith(MockitoJUnitRunner.class)
public class DoedsmeldingSkdParameterStrategyTest {

    private static final String FNR = "01234567890";
    private static final LocalDateTime REGDATO = LocalDateTime.of(2017, 9, 29, 15, 38, 0);
    private static final LocalDate DOEDSDATO = LocalDate.of(2017, 10, 2);

    private static final String REGDATO_DATE_STRING = "20170929";
    private static final String REGDATO_TIME_STRING = "153800";
    private static final String DOEDSDATO_DATE_STRING = "20171002";

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
        when(aPerson.getIdenttype()).thenReturn(IdentType.FNR.name());
        when(aPerson.getRegdato()).thenReturn(REGDATO);
        when(aPerson.getDoedsdato()).thenReturn(DOEDSDATO.atTime(12, 00));
    }

    @Test
    public void isSupportedTest() {
        SkdParametersCreator correctObject = mock(DoedsmeldingSkdParametere.class);
        SkdParametersCreator incorrectObject = mock(InnvandringCreateSkdParametere.class);

        assertThat(doedsmeldingSkdParameterStrategy.isSupported(correctObject), is(true));
        assertThat(doedsmeldingSkdParameterStrategy.isSupported(incorrectObject), is(false));
    }

    @Test
    public void createCorrectSkdParameterMapFromPerson() {
        SkdMeldingTrans1 result = doedsmeldingSkdParameterStrategy.execute(aPerson);

        assertThat(result.getFodselsdato(), is(equalTo(FNR.substring(0, 6))));
        assertThat(result.getPersonnummer(), is(equalTo(FNR.substring(6, 11))));
        assertThat(result.getMaskintid(), is(equalTo(REGDATO_TIME_STRING)));
        assertThat(result.getMaskindato(), is(equalTo(REGDATO_DATE_STRING)));
        assertThat(result.getRegDato(), is(equalTo(DOEDSDATO_DATE_STRING)));
        assertThat(result.getDatoDoed(), is(equalTo(DOEDSDATO_DATE_STRING)));
        assertThat(result.getAarsakskode(), is(equalTo(AARSAKSKODE_FOR_DOEDSMELDING)));
        assertThat(result.getTranstype(), is(equalTo(TRANSTYPE_FOR_DOEDSMELDING)));
        assertThat(result.getStatuskode(), is(equalTo(STATUSKODE_FOR_DOEDSMELDING)));
    }
}

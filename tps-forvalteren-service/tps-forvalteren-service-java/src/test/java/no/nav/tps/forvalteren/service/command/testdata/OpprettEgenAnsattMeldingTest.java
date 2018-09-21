package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreEgenAnsatt.EGEN_ANSATT_MLD_NAVN;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreEgenansattRequest;
import no.nav.tps.forvalteren.service.command.testdata.skd.TpsNavEndringsMelding;

@RunWith(MockitoJUnitRunner.class)
public class OpprettEgenAnsattMeldingTest {

    private final static String IDENT = "11111100000";

    @InjectMocks
    private OpprettEgenAnsattMelding opprettEgenAnsattMelding;

    private Person person;

    private String stringLocalDateTimeFom;

    @Before
    public void setup() {
        person = new Person();
        LocalDateTime localDateTimeFom = LocalDateTime.of(2018, 06, 01, 12, 00);
        stringLocalDateTimeFom = "2018-06-01";

        person.setIdent(IDENT);
        person.setEgenAnsattDatoFom(localDateTimeFom);
        person.setEgenAnsattDatoTom(localDateTimeFom);
    }

    @Test
    public void opprettEgenAnsattMeldingTest() {

        List<TpsNavEndringsMelding> result = opprettEgenAnsattMelding.execute(person, new HashSet(Arrays.asList("u5")));
    
        TpsEndreEgenansattRequest melding = (TpsEndreEgenansattRequest) result.get(0).getMelding();
        assertThat(melding.getFom(), is(stringLocalDateTimeFom));
        assertThat(melding.getServiceRutinenavn(), is("endre_egen_ansatt"));
        assertThat(melding.getOffentligIdent(), is(IDENT));
    }

    @Test
    public void opprettEgenAnsattMeldingTilFlereMiljoTest() {

        List<TpsNavEndringsMelding> result = opprettEgenAnsattMelding.execute(person, new HashSet(Arrays.asList("u5", "u6", "t1")));

        assertThat(result.size(), is(3));
        assertThat(result.get(0).getMiljo(), is("u5"));
        assertThat(result.get(2).getMiljo(), is("t1"));
    }

    @Test
    public void buildRequest() {

        TpsEndreEgenansattRequest result = opprettEgenAnsattMelding.buildRequest(person);

        assertThat(result.getOffentligIdent(), is(IDENT));
        assertThat(result.getFom(), is(stringLocalDateTimeFom));
        assertThat(result.getTom(), is(stringLocalDateTimeFom));
        assertThat(result.getServiceRutinenavn(), is(EGEN_ANSATT_MLD_NAVN));
    }
}

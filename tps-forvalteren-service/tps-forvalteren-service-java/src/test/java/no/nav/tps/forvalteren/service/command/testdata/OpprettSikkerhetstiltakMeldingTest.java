package no.nav.tps.forvalteren.service.command.testdata;

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
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreSikkerhetstiltakRequest;
import no.nav.tps.forvalteren.service.command.testdata.skd.TpsNavEndringsMelding;

@RunWith(MockitoJUnitRunner.class)
public class OpprettSikkerhetstiltakMeldingTest {


    @InjectMocks
    private OpprettSikkerhetstiltakMelding opprettSikkerhetstiltakMelding;

    private Person person;

    private String stringLocalDateTimeFom;
    private String stringLocalDateTimeTom;

    @Before
    public void setup() {

        person = new Person();

        LocalDateTime localDateTimeFom = LocalDateTime.of(2018, 06, 01, 12, 00);
        LocalDateTime localDateTimeTom = LocalDateTime.of(2020, 01, 01, 12, 00);

        stringLocalDateTimeFom = "2018-06-01";
        stringLocalDateTimeTom = "2020-01-01";

        person.setIdent("11111100000");
        person.setSikkerhetsTiltakDatoFom(localDateTimeFom);
        person.setSikkerhetsTiltakDatoTom(localDateTimeTom);
        person.setTypeSikkerhetsTiltak("ABCD");
        person.setBeskrSikkerhetsTiltak("en beskrivelse");

    }

    @Test
    public void opprettSikkerhetsTiltaksMeldingTest() {

        List<TpsNavEndringsMelding> result = opprettSikkerhetstiltakMelding.execute(person, new HashSet(Arrays.asList("u5")));
        TpsEndreSikkerhetstiltakRequest melding = (TpsEndreSikkerhetstiltakRequest) result.get(0).getMelding();
        assertThat(melding.getFom(), is(stringLocalDateTimeFom));
        assertThat(melding.getTom(), is(stringLocalDateTimeTom));
        assertThat(melding.getOffentligIdent(), is("11111100000"));
        assertThat(melding.getTypeSikkerhetsTiltak(), is("ABCD"));
        assertThat(melding.getServiceRutinenavn(), is("endre_sikkerhetstiltak"));

    }

    @Test
    public void opprettSikkerhetsTiltaksMeldingIFlereMiljoTest() {

        List<TpsNavEndringsMelding> result = opprettSikkerhetstiltakMelding.execute(person, new HashSet(Arrays.asList("u5", "t9", "q8", "t2")));

        assertThat(result.size(), is(4));
        assertThat(result.get(0).getMiljo(), is("u5"));
        assertThat(result.get(1).getMiljo(), is("t9"));
        assertThat(result.get(3).getMiljo(), is("t2"));
    }
}

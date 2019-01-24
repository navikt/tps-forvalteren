package no.nav.tps.forvalteren.service.command.testdata;

import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreSikkerhetstiltakRequest;
import no.nav.tps.forvalteren.service.command.testdata.skd.TpsNavEndringsMelding;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@RunWith(MockitoJUnitRunner.class)
public class OpprettSikkerhetstiltakMeldingTest {

    private static final LocalDateTime DATE_FOM = LocalDateTime.of(2018, 06, 01, 12, 00);
    private static final LocalDateTime DATE_TOM = LocalDateTime.of(2020, 01, 01, 12, 00);

    @InjectMocks
    private OpprettSikkerhetstiltakMelding opprettSikkerhetstiltakMelding;

    private Person person;

    @Before
    public void setup() {
        person = Person.builder()
                .ident("11111100000")
                .sikkerhetsTiltakDatoFom(DATE_FOM)
                .sikkerhetsTiltakDatoTom(DATE_TOM)
                .typeSikkerhetsTiltak("ABCD")
                .beskrSikkerhetsTiltak("en beskrivelse")
                .build();
    }

    @Test
    public void opprettSikkerhetsTiltaksMeldingTest() {

        List<TpsNavEndringsMelding> result = opprettSikkerhetstiltakMelding.execute(person, singleton("u5"));
        TpsEndreSikkerhetstiltakRequest melding = (TpsEndreSikkerhetstiltakRequest) result.get(0).getMelding();
        assertThat(melding.getFom(), is(ConvertDateToString.yyyysMMsdd(DATE_FOM)));
        assertThat(melding.getTom(), is(ConvertDateToString.yyyysMMsdd(DATE_TOM)));
        assertThat(melding.getOffentligIdent(), is("11111100000"));
        assertThat(melding.getTypeSikkerhetsTiltak(), is("ABCD"));
        assertThat(melding.getServiceRutinenavn(), is("endre_sikkerhetstiltak"));
    }
}

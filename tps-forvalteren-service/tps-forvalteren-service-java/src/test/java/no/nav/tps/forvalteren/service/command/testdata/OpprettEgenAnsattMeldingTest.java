package no.nav.tps.forvalteren.service.command.testdata;

import static java.time.LocalDateTime.of;
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
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreEgenansattRequest;
import no.nav.tps.forvalteren.service.command.testdata.skd.TpsNavEndringsMelding;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@RunWith(MockitoJUnitRunner.class)
public class OpprettEgenAnsattMeldingTest {

    private static final String IDENT = "11111100000";
    private static final LocalDateTime DATE_FOM = of(2018, 06, 01, 12, 00);
    private static final LocalDateTime DATE_TOM = of(2019, 01, 10, 12, 00);

    @InjectMocks
    private OpprettEgenAnsattMelding opprettEgenAnsattMelding;

    private Person person;

    @Before
    public void setup() {
        person = Person.builder()
                .ident(IDENT)
                .egenAnsattDatoFom(DATE_FOM)
                .egenAnsattDatoTom(DATE_TOM)
                .build();
    }

    @Test
    public void opprettEgenAnsattMeldingTest() {

        List<TpsNavEndringsMelding> result = opprettEgenAnsattMelding.execute(person, singleton("u5"));
    
        TpsEndreEgenansattRequest melding = (TpsEndreEgenansattRequest) result.get(0).getMelding();
        assertThat(melding.getFom(), is(ConvertDateToString.yyyysMMsdd(DATE_FOM)));
        assertThat(melding.getTom(), is(ConvertDateToString.yyyysMMsdd(DATE_TOM)));
        assertThat(melding.getServiceRutinenavn(), is("endre_egen_ansatt"));
        assertThat(melding.getOffentligIdent(), is(IDENT));
    }
}

package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.UtvandringSkdParametere;

@RunWith(MockitoJUnitRunner.class)
public class UtvandringsSkdParameterStrategyTest {

    private final static String IDENT = "23104723456";
    private final static LocalDateTime DATE = LocalDateTime.of(2000, 12, 14, 0, 0);


    @Mock
    private Person person;

    @InjectMocks
    private UtvandringsSkdParameterStrategy utvandringsSkdParameterStrategy;

    @Test
    public void hentTildelingskode() throws Exception {
        assertThat(utvandringsSkdParameterStrategy.hentTildelingskode(), is("0"));
    }

    @Test
    public void isSupported() throws Exception {
        assertThat(utvandringsSkdParameterStrategy.isSupported(new UtvandringSkdParametere()), is(true));
    }

    @Test
    public void execute() throws Exception {
        when(person.getIdent()).thenReturn(IDENT);
        when(person.getRegdato()).thenReturn(DATE);
        utvandringsSkdParameterStrategy.execute(person);
    }
}
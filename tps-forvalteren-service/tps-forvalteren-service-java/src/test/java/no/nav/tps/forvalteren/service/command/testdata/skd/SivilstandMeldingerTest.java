package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Sivilstand;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies.SivilstandSkdParameterStrategy;

@RunWith(MockitoJUnitRunner.class)
public class SivilstandMeldingerTest {

    private static final String IDENT = "12345678901";
    private static final Person PERSON_RELASJON_MED = Person.builder().ident(IDENT).build();

    @Mock
    private SivilstandSkdParameterStrategy sivilstandSkdParameterStrategy;

    @Mock
    private SkdGetHeaderForSkdMelding skdGetHeaderForSkdMelding;

    @InjectMocks
    private SivilstandMeldinger sivilstandMeldinger;

    @Test
    public void createMeldinger_emptyHistorikk() {

        sivilstandMeldinger.createMeldinger(singletonList(Person.builder().build()), true);

        verify(sivilstandSkdParameterStrategy, never()).execute(any(Person.class));
        verify(skdGetHeaderForSkdMelding, never()).execute(any(SkdMeldingTrans1.class));
    }

    @Test
    public void createMeldinger_Historikk() {

        Person person = Person.builder()
                .sivilstander(asList(
                        Sivilstand.builder()
                                .sivilstand("GIFT")
                                .personRelasjonMed(PERSON_RELASJON_MED)
                                .build(),
                        Sivilstand.builder()
                                .sivilstand("SKILT")
                                .build()
                ))
                .build();

        when(sivilstandSkdParameterStrategy.execute(person)).thenReturn(singletonList(new SkdMeldingTrans1()));

        sivilstandMeldinger.createMeldinger(singletonList(person), true);

        verify(sivilstandSkdParameterStrategy).execute(any(Person.class));
        verify(skdGetHeaderForSkdMelding).execute(any(SkdMeldingTrans1.class));
    }
}
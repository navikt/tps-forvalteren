package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aFemalePerson;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

@RunWith(MockitoJUnitRunner.class)
public class FinnBarnTilForelderTest {

    @InjectMocks
    private FinnBarnTilForelder finnBarnTilForelder;

    @Mock
    private RelasjonRepository relasjonRepository;

    private Person forelder = aMalePerson().build();
    private List<Person> barn = new ArrayList<>();
    private List<Relasjon> relasjoner = new ArrayList<>();

    @Before
    public void setup() {
        Person barnMann = aFemalePerson().build();
        Person barnKvinne = aMalePerson().build();
        barn.add(barnKvinne);
        barn.add(barnMann);
        Relasjon relasjon = new Relasjon(0L, forelder, barnKvinne, "BARN");
        Relasjon relasjon2 = new Relasjon(0L, forelder, barnMann, "BARN");
        relasjoner.add(relasjon);
        relasjoner.add(relasjon2);
    }

    @Test
    public void checkThatExecuteReturnsCorrectBarn() {
        when(relasjonRepository.findByPersonAndRelasjonTypeNavn(forelder, "BARN")).thenReturn(relasjoner);

        List<Person> result = finnBarnTilForelder.execute(forelder);

        assertThat(result.size(), is(2));
        assertThat(result.get(0), is(barn.get(0)));
        assertThat(result.get(1), is(barn.get(1)));
    }

    @Test
    public void checkThatNoBarnIsReturnedWhenNoRelasjon() {
        when(relasjonRepository.findByPersonAndRelasjonTypeNavn(forelder, "BARN")).thenReturn(new ArrayList<>());

        List<Person> result = finnBarnTilForelder.execute(forelder);

        assertThat(result, is(empty()));
    }

}
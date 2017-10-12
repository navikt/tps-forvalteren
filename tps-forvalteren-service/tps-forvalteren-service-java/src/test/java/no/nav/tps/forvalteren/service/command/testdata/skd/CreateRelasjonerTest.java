package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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
public class CreateRelasjonerTest {

    @InjectMocks
    private CreateRelasjoner createRelasjoner;

    @Mock
    private RelasjonRepository relasjonRepository;

    @Mock
    private SkdMessageSenderTrans1 skdMessageSenderTrans1;

    @Mock
    private PersistBarnTransRecordsToTps persistBarnTransRecordsToTps;

    private List<String> environments = new ArrayList<>(Arrays.asList("u2", "u6"));

    private Person person = aMalePerson().id(1L).build();
    private Person person2 = aMalePerson().id(2L).build();
    private Person person3 = aMalePerson().id(3L).build();
    private List<Person> personerSomIkkeEksitererITpsMiljoe = new ArrayList<>(Arrays.asList(person, person2, person3));

    private Relasjon relasjonFar = new Relasjon(1L, person, person2, "FAR");
    private Relasjon relasjonBarn = new Relasjon(2L, person2, person, "BARN");
    private Relasjon relasjonEktefelle = new Relasjon(3L, person2, person3, "EKTEFELLE");
    private Relasjon relasjonEktefelle2 = new Relasjon(4L, person3, person2, "EKTEFELLE");

    @Before
    public void setup() {

    }

    @Test
    public void checkThatAllServicesGetsCalled() {
        when(relasjonRepository.findByPersonId(person.getId())).thenReturn(Arrays.asList(relasjonFar));
        when(relasjonRepository.findByPersonId(person2.getId())).thenReturn(Arrays.asList(relasjonBarn, relasjonEktefelle));
        when(relasjonRepository.findByPersonId(person3.getId())).thenReturn(Arrays.asList(relasjonEktefelle2));

        createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, environments);

        verify(relasjonRepository, times(2)).findByPersonId(person.getId());
        verify(relasjonRepository, times(2)).findByPersonId(person2.getId());
        verify(relasjonRepository, times(2)).findByPersonId(person3.getId());
        verify(skdMessageSenderTrans1).execute("Vigsel", Arrays.asList(person2), environments);
        verify(skdMessageSenderTrans1).execute("Vigsel", Arrays.asList(person3), environments);
        verify(persistBarnTransRecordsToTps).execute(person2, environments);
    }

    @Test
    public void checkThatOnlyVigelseGetsCalled() {
        when(relasjonRepository.findByPersonId(person2.getId())).thenReturn(Arrays.asList(relasjonEktefelle));
        when(relasjonRepository.findByPersonId(person3.getId())).thenReturn(Arrays.asList(relasjonEktefelle2));

        createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, environments);

        verify(relasjonRepository, times(1)).findByPersonId(person.getId());
        verify(relasjonRepository, times(2)).findByPersonId(person2.getId());
        verify(relasjonRepository, times(2)).findByPersonId(person3.getId());
        verify(skdMessageSenderTrans1).execute("Vigsel", Arrays.asList(person2), environments);
        verify(skdMessageSenderTrans1).execute("Vigsel", Arrays.asList(person3), environments);
    }

    @Test
    public void checkThatOnlyFamilieendringGetsCalled() {
        when(relasjonRepository.findByPersonId(person.getId())).thenReturn(Arrays.asList(relasjonFar));
        when(relasjonRepository.findByPersonId(person2.getId())).thenReturn(Arrays.asList(relasjonBarn));

        createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, environments);

        verify(relasjonRepository, times(2)).findByPersonId(person.getId());
        verify(relasjonRepository, times(2)).findByPersonId(person2.getId());
        verify(relasjonRepository, times(1)).findByPersonId(person3.getId());
        verify(persistBarnTransRecordsToTps).execute(person2, environments);
    }

    @Test
    public void checkThatNothingGetsCalled() {
        createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, environments);

        verify(skdMessageSenderTrans1, never()).execute(anyString(), anyListOf(Person.class), anyListOf(String.class));
    }

}
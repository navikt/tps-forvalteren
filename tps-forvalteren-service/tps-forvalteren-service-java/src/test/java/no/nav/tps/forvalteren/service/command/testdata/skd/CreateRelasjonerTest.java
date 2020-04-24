package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Arrays.asList;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

@RunWith(MockitoJUnitRunner.class)
public class CreateRelasjonerTest {

    private static final boolean ADD_HEADER = true;

    @InjectMocks
    private CreateRelasjoner createRelasjoner;

    @Mock
    private RelasjonRepository relasjonRepository;

    @Mock
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Mock
    private PersistBarnTransRecordsToTps persistBarnTransRecordsToTps;

    private Person person = aMalePerson().id(1L).build();
    private Person person2 = aMalePerson().id(2L).build();
    private Person person3 = aMalePerson().id(3L).build();
    private List<Person> personerSomIkkeEksitererITpsMiljoe = new ArrayList<>(asList(person, person2, person3));

    private Relasjon relasjonFar = new Relasjon(1L, person, person2, "FAR");
    private Relasjon relasjonBarn = new Relasjon(2L, person2, person, "BARN");
    private Relasjon relasjonEktefelle = new Relasjon(3L, person2, person3, "EKTEFELLE");
    private Relasjon relasjonEktefelle2 = new Relasjon(4L, person3, person2, "EKTEFELLE");

    @Test
    public void checkThatAllServicesGetsCalled() {
        when(relasjonRepository.findByPersonId(person.getId())).thenReturn(asList(relasjonFar));
        when(relasjonRepository.findByPersonId(person2.getId())).thenReturn(asList(relasjonBarn, relasjonEktefelle));
        when(relasjonRepository.findByPersonId(person3.getId())).thenReturn(asList(relasjonEktefelle2));

        createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, ADD_HEADER);

        verify(skdMessageCreatorTrans1).execute("Vigselsmelding", asList(person2), ADD_HEADER);
        verify(skdMessageCreatorTrans1).execute("Vigselsmelding", asList(person3), ADD_HEADER);
        verify(persistBarnTransRecordsToTps).execute(person2, ADD_HEADER);
    }

    @Test
    public void checkThatOnlyVigelseGetsCalled() {
        when(relasjonRepository.findByPersonId(person2.getId())).thenReturn(asList(relasjonEktefelle));
        when(relasjonRepository.findByPersonId(person3.getId())).thenReturn(asList(relasjonEktefelle2));

        createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, ADD_HEADER);

        verify(skdMessageCreatorTrans1).execute("Vigselsmelding", asList(person2), ADD_HEADER);
        verify(skdMessageCreatorTrans1).execute("Vigselsmelding", asList(person3), ADD_HEADER);
    }

    @Test
    public void checkThatOnlyFamilieendringGetsCalled() {
        when(relasjonRepository.findByPersonId(person.getId())).thenReturn(asList(relasjonFar));
        when(relasjonRepository.findByPersonId(person2.getId())).thenReturn(asList(relasjonBarn));

        createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, ADD_HEADER);

        verify(persistBarnTransRecordsToTps).execute(person2, ADD_HEADER);
    }

    @Test
    public void checkThatNothingGetsCalled() {
        createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, ADD_HEADER);

        verify(skdMessageCreatorTrans1, never()).execute(anyString(), anyList(), anyBoolean());
    }

}
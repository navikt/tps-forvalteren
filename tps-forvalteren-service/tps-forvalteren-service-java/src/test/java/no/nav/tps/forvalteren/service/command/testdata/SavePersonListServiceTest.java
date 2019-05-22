package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.PostadresseRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentUtdaterteRelasjonIder;
import no.nav.tps.forvalteren.service.command.testdata.utils.OppdaterRelasjonReferanser;

@RunWith(MockitoJUnitRunner.class)
public class SavePersonListServiceTest {

    @InjectMocks
    private SavePersonListService savePersonListService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AdresseRepository adresseRepository;

    @Mock
    private PostadresseRepository postadresseRepository;

    @Mock
    private RelasjonRepository relasjonRepository;

    @Mock
    private OppdaterRelasjonReferanser oppdaterRelasjonReferanser;

    @Mock
    private HentUtdaterteRelasjonIder hentUtdaterteRelasjonIder;

    @Mock
    private AdresseOgSpesregService adresseOgSpesregService;

    private List<Person> persons = new ArrayList<>();
    private Postadresse postadresse = new Postadresse();
    private Adresse boadresse = new Gateadresse();
    private Long utdatertRelasjonId = 1337L;
    private Set<Long> utdaterteRelasjonIder = new HashSet<>(Arrays.asList(utdatertRelasjonId));

    private Person person = aMalePerson()
            .postadresse(Arrays.asList(postadresse))
            .boadresse(boadresse).build();

    @Before
    public void setup() {

        person.setOpprettetAv("Gud");
        person.setOpprettetDato(LocalDateTime.now());
        person.setEndretAv("Jesus");
        person.setEndretDato(LocalDateTime.now());
        persons.add(person);

        when(personRepository.findById(person.getId())).thenReturn(person);
        when(hentUtdaterteRelasjonIder.execute(person, person)).thenReturn(utdaterteRelasjonIder);
    }

    @Test
    public void verifyThatAllServicesGetsCalled() {
        savePersonListService.execute(persons);

        verify(personRepository).findById(person.getId());
        verify(oppdaterRelasjonReferanser).execute(person, person);
        verify(hentUtdaterteRelasjonIder).execute(person, person);
        verify(adresseRepository).deleteAllByPerson(person);
        verify(personRepository).save(persons.get(0));
        verify(relasjonRepository).deleteByIdIn(utdaterteRelasjonIder);

    }

    @Test
    public void verifyThatMandatoryServicesGetsCalled() {
        persons.get(0).setBoadresse(null);
        when(personRepository.findById(person.getId())).thenReturn(null);

        savePersonListService.execute(persons);

        verify(personRepository).findById(person.getId());
        verify(oppdaterRelasjonReferanser, never()).execute(person, person);
        verify(hentUtdaterteRelasjonIder, never()).execute(person, person);
        verify(adresseRepository, never()).deleteAllByPerson(person);
        verify(personRepository).save(persons.get(0));
        verify(relasjonRepository, never()).deleteByIdIn(utdaterteRelasjonIder);

    }

}
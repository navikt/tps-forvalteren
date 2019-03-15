package no.nav.tps.forvalteren.service.command.testdata;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentUtdaterteRelasjonIder;
import no.nav.tps.forvalteren.service.command.testdata.utils.OppdaterRelasjonReferanser;

@RunWith(SpringJUnit4ClassRunner.class)
public class UpdatePersonServiceTest {

    private static final Long ID = 1L;
    private static final String IDENT = "12042078901";

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AdresseRepository adresseRepository;

    @Mock
    private RelasjonRepository relasjonRepository;

    @Mock
    private OppdaterRelasjonReferanser oppdaterRelasjonReferanser;

    @Mock
    private HentUtdaterteRelasjonIder hentUtdaterteRelasjonIder;

    @Mock
    private AdresseOgSpesregService adresseOgSpesregService;

    @InjectMocks
    private UpdatePersonService updatePersonService;

    @Test
    public void update_whenPersonExists_callUtilServices() {

        Person person = Person.builder()
                .id(ID)
                .ident(IDENT)
                .build();
        when(personRepository.findById(ID)).thenReturn(person);

        updatePersonService.update(person);

        verify(oppdaterRelasjonReferanser).execute(person, person);
        verify(hentUtdaterteRelasjonIder).execute(person, person);
        verify(adresseRepository).deleteAllByPerson(person);
        verify(adresseOgSpesregService).updateAdresseOgSpesregAttributes(person);

        verify(personRepository).save(person);
    }

    @Test
    public void update_whenPersonDontExist_thenDontCallUtilServices() {

        Person person = Person.builder()
                .id(ID)
                .ident(IDENT)
                .build();
        when(personRepository.findById(ID)).thenReturn(null);

        updatePersonService.update(person);

        verify(oppdaterRelasjonReferanser, times(0)).execute(person, person);
        verify(hentUtdaterteRelasjonIder, times(0)).execute(person, person);
        verify(adresseRepository, times(0)).deleteAllByPerson(person);
        verify(personRepository).save(person);
    }
}
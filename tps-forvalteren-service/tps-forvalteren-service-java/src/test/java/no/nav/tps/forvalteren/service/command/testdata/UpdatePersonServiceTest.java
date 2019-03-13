package no.nav.tps.forvalteren.service.command.testdata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentUtdaterteRelasjonIder;
import no.nav.tps.forvalteren.service.command.testdata.utils.OppdaterRelasjonReferanser;

@RunWith(SpringJUnit4ClassRunner.class)
@Import(HentDatoFraIdentService.class)
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
    private UpdateAdresseOgSpesregForUfb updateAdresseOgSpesregForUfb;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @InjectMocks
    private UpdatePersonService updatePersonService;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(updatePersonService, "hentDatoFraIdentService", hentDatoFraIdentService);
    }

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
        verify(updateAdresseOgSpesregForUfb).updateAttributesForUfb(person, person);

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
        verify(updateAdresseOgSpesregForUfb, times(0)).updateAttributesForUfb(person, person);
        verify(personRepository).save(person);
    }

    @Test
    public void update_whenCalled_produceMissingAddress() {

        Person person = Person.builder()
                .id(ID)
                .ident(IDENT)
                .build();
        when(personRepository.findById(ID)).thenReturn(person);

        updatePersonService.update(person);

        assertThat(person.getBoadresse().getKommunenr(), is(equalTo("0301")));
        assertThat(person.getBoadresse().getPostnr(), is(equalTo("0557")));
        assertThat(person.getBoadresse().getFlyttedato(), is(equalTo(LocalDateTime.of(2020, 04, 12, 0, 0))));
        assertThat(((Gateadresse) person.getBoadresse()).getAdresse(), is(equalTo("SANNERGATA")));
        assertThat(((Gateadresse) person.getBoadresse()).getHusnummer(), is(equalTo("2")));
        assertThat(((Gateadresse) person.getBoadresse()).getGatekode(), is(equalTo("16188")));
        assertThat(person.getBoadresse().getPerson(), is(equalTo(person)));
    }
}
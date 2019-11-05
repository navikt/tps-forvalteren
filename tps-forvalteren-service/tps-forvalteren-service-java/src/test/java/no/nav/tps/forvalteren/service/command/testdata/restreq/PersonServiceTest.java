package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.singletonList;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.IdenthistorikkRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.repository.jpa.SivilstandRepository;
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.TpsPersonService;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {

    private static final String IDENT1 = "12345678901";
    private static final Long ID = 31221L;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AdresseRepository adresseRepository;

    @Mock
    private RelasjonRepository relasjonRepository;

    @Mock
    private DoedsmeldingRepository doedsmeldingRepository;

    @Mock
    private IdenthistorikkRepository identhistorikkRepository;

    @Mock
    private SivilstandRepository sivilstandRepository;

    @Mock
    private IdentpoolService identpoolService;

    @Mock
    private TpsPersonService tpsPersonService;

    @InjectMocks
    private PersonService personService;

    @Test(expected = NotFoundException.class)
    public void deletePersons_NotFound() {

        personService.deletePersons(new ArrayList<>(), newArrayList(IDENT1));
    }

    @Test
    public void deletePersons_OK() {

        when(personRepository.findByIdentIn(anyList())).thenReturn(singletonList(Person.builder().build()));
        when(relasjonRepository.findByPersonRelasjonMedIdIn(anyList())).thenReturn(Optional.of(newArrayList(Relasjon.builder()
                .id(ID)
                .person(Person.builder().ident(IDENT1).build())
                .build())));
        Gateadresse gateadresse = Gateadresse.builder().build();
        gateadresse.setId(ID);
        when(adresseRepository.findAdresseByPersonIdIn(anyList())).thenReturn(Optional.of(newArrayList(gateadresse)));

        personService.deletePersons(new ArrayList<>(), newArrayList(IDENT1));

        verify(relasjonRepository).deleteByIdIn(anySet());
        verify(sivilstandRepository).deleteByIdIn(anySet());
        verify(adresseRepository).deleteByIdIn(anyList());
        verify(doedsmeldingRepository).deleteByPersonIdIn(anyList());
        verify(personRepository).deleteByIdIn(anyList());
        verify(tpsPersonService).sendDeletePersonMeldinger(anyList() ,anySet());
        verify(identpoolService).recycleIdents(anySet());
    }

    @Test
    public void deleteIdenthistorikk() {

        Person person = Person.builder()
                .identHistorikk(newArrayList(IdentHistorikk.builder()
                        .id(ID)
                        .aliasPerson(Person.builder().build())
                        .build()))
                .build();

        personService.deleteIdenthistorikk(singletonList(person));

        verify(identhistorikkRepository).deleteByIdIn(anyList());
    }
}
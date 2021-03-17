package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.FullmaktRepository;
import no.nav.tps.forvalteren.repository.jpa.IdenthistorikkRepository;
import no.nav.tps.forvalteren.repository.jpa.InnvandretUtvandretRepository;
import no.nav.tps.forvalteren.repository.jpa.MidlertidigAdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.PostadresseRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.repository.jpa.SivilstandRepository;
import no.nav.tps.forvalteren.repository.jpa.StatsborgerskapRepository;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
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
    private PostadresseRepository postadresseRepository;

    @Mock
    private MidlertidigAdresseRepository midlertidigAdresseRepository;

    @Mock
    private RelasjonRepository relasjonRepository;

    @Mock
    private DoedsmeldingRepository doedsmeldingRepository;

    @Mock
    private IdenthistorikkRepository identhistorikkRepository;

    @Mock
    private VergemaalRepository vergemaalRepository;

    @Mock
    private FullmaktRepository fullmaktRepository;

    @Mock
    private InnvandretUtvandretRepository innvandretUtvandretRepository;

    @Mock
    private StatsborgerskapRepository statsborgerskapRepository;

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

        when(personRepository.findByIdentIn(anySet())).thenReturn(singletonList(Person.builder().build()));

        personService.deletePersons(new ArrayList<>(), newArrayList(IDENT1));

        verify(relasjonRepository).deleteByIdIn(anySet());
        verify(sivilstandRepository).deleteByIdIn(anySet());
        verify(adresseRepository).deleteByIdIn(anySet());
        verify(postadresseRepository).deleteByIdIn(anySet());
        verify(midlertidigAdresseRepository).deleteByIdIn(anySet());
        verify(innvandretUtvandretRepository).deleteByIdIn(anySet());
        verify(statsborgerskapRepository).deleteByIdIn(anySet());
        verify(fullmaktRepository).deleteByIdIn(anySet());
        verify(vergemaalRepository).deleteByIdIn(anySet());
        verify(doedsmeldingRepository).deleteByPersonIdIn(any());
        verify(personRepository).deleteByIdIn(anySet());
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

        verify(identhistorikkRepository).deleteByIdIn(anySet());
    }
}
package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.anyLong;
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
import no.nav.tps.forvalteren.repository.jpa.IdenthistorikkRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
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
    private IdentpoolService identpoolService;

    @Mock
    private TpsPersonService tpsPersonService;

    @Mock
    private DeletePersonService deletePersonService;

    @Mock
    private IdenthistorikkRepository identhistorikkRepository;

    @InjectMocks
    private PersonService personService;

    @Test(expected = NotFoundException.class)
    public void deletePersons_NotFound() {

        personService.deletePersons(new ArrayList<>(), newArrayList(IDENT1));
    }

    @Test
    public void deletePersons_OK() {

        when(personRepository.findByIdentIn(anySet())).thenReturn(singletonList(Person.builder().id(1L).build()));

        personService.deletePersons(new ArrayList<>(), newArrayList(IDENT1));

        verify(tpsPersonService).sendDeletePersonMeldinger(anyList() ,anySet());
        verify(deletePersonService).execute(anyLong());
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
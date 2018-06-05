package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.util.ArrayList;
import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FindVergemaalIdsFromPersonIdsTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private VergemaalRepository vergemaalRepository;

    @Mock
    private Person person;

    @Mock
    private Vergemaal vergemaal;

    @InjectMocks
    private FindVergemaalIdsFromPersonIds findVergemaalIdsFromPersonIds;

    private List<Long> listeMedId;
    private List<Person> listeMedPersoner;
    private List<Vergemaal> listeMedVergemaal;

    private String ident = "01234512345";
    private Long id = 0000001L;

    @Before
    public void setup() {
        listeMedId = new ArrayList<>();
        listeMedPersoner = new ArrayList<>();
        listeMedVergemaal = new ArrayList<>();

        when(personRepository.findByIdIn(anyListOf(Long.class))).thenReturn(listeMedPersoner);
        when(vergemaalRepository.findAllByIdentIn(anyListOf(String.class))).thenReturn(listeMedVergemaal);
    }

    @Test
    public void execute() {

        listeMedId.add(id);

        Person testPerson = new Person();
        testPerson.setIdent(ident);
        listeMedPersoner.add(testPerson);

        Vergemaal vergemaal = new Vergemaal();
        vergemaal.setId(id);
        listeMedVergemaal.add(vergemaal);

        List<Long> resultat = findVergemaalIdsFromPersonIds.execute(listeMedId);

        assertThat(resultat.size(), is(1));

        verify(personRepository).findByIdIn(anyList());
        verify(vergemaalRepository).findAllByIdentIn(anyList());

    }
}

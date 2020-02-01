package no.nav.tps.forvalteren.service.command.testdata.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;

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

    private static final String ident = "01234512345";
    private static final Long id = 0000001L;

    private List<Long> listeMedId;
    private List<Person> listeMedPersoner;
    private List<Vergemaal> listeMedVergemaal;

    @Before
    public void setup() {
        listeMedId = new ArrayList<>();
        listeMedPersoner = new ArrayList<>();
        listeMedVergemaal = new ArrayList<>();

        when(personRepository.findByIdIn(anyList())).thenReturn(listeMedPersoner);
        when(vergemaalRepository.findAllByIdentIn(anyList())).thenReturn(listeMedVergemaal);
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

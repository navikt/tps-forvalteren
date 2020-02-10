package no.nav.tps.forvalteren.service.command.testdata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.opprett.FindIdenterNotUsedInDB;
import no.nav.tps.forvalteren.service.command.testdata.restreq.PersonService;

@RunWith(MockitoJUnitRunner.class)
public class FindAllExistingIdenterInDBTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;

    private List<Person> existingIdenterInDB;
    private List<String> newIdenter;

    @Before
    public void before() {
        existingIdenterInDB = new ArrayList<>();
        Person person = new Person();
        person.setIdent("12345678910");
        Person person2 = new Person();
        person2.setIdent("12345678911");
        existingIdenterInDB.add(person);
        existingIdenterInDB.add(person2);

        newIdenter = new ArrayList<>();
        newIdenter.add("12345678910");
        newIdenter.add("12345678911");
        newIdenter.add("12345678912");
    }

    @Test
    public void removesExistingIdenter() {
        when(personService.getPersonerByIdenter(any(List.class))).thenReturn(existingIdenterInDB);
        Set<String> result = findIdenterNotUsedInDB.filtrer(new HashSet<>(newIdenter));
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(newIdenter.get(2)));
    }

}
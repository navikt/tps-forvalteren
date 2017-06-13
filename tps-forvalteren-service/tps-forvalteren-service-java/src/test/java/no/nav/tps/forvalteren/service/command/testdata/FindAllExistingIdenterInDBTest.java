package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.opprett.FindIdenterNotUsedInDB;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FindAllExistingIdenterInDBTest {

    @Mock
    private PersonRepository repository;

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
        when(repository.findByIdentIn(any(List.class))).thenReturn(existingIdenterInDB);
        Set<String> result = findIdenterNotUsedInDB.filtrer(new HashSet<>(newIdenter));
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(newIdenter.get(2)));
    }

}
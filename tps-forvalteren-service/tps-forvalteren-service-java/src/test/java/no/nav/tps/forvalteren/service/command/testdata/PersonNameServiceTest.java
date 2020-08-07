package no.nav.tps.forvalteren.service.command.testdata;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;

@RunWith(MockitoJUnitRunner.class)
public class PersonNameServiceTest {

    private PersonNameService command;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    private ArrayList<Person> people;
    private Person personMock1;
    private Person personMock2;

    @Before
    public void before() {
        command = new PersonNameService();
        personMock1 = mock(Person.class);
        personMock2 = mock(Person.class);

        people = new ArrayList<>();
        people.add(personMock1);
        people.add(personMock2);
    }

    @Test
    public void fornavnTest() {
        command.execute(people);

        verify(personMock1).setFornavn(stringCaptor.capture());
        verify(personMock2).setFornavn(stringCaptor.capture());

        assertThat(stringCaptor.getAllValues().get(0), not(is(emptyOrNullString())));
        assertThat(stringCaptor.getAllValues().get(1), not(is(emptyOrNullString())));
    }

    @Test
    public void mellomnavnTest() {
        command.execute(personMock1, true);
        command.execute(personMock2, true);

        verify(personMock1).setMellomnavn(stringCaptor.capture());
        verify(personMock2).setMellomnavn(stringCaptor.capture());

        assertThat(stringCaptor.getAllValues().get(0), not(is(emptyOrNullString())));
        assertThat(stringCaptor.getAllValues().get(1), not(is(emptyOrNullString())));
    }

    @Test
    public void etternavnTest() {
        command.execute(people);

        verify(personMock1).setEtternavn(stringCaptor.capture());
        verify(personMock2).setEtternavn(stringCaptor.capture());

        assertThat(stringCaptor.getAllValues().get(0), not(is(emptyOrNullString())));
        assertThat(stringCaptor.getAllValues().get(1), not(is(emptyOrNullString())));
    }

}
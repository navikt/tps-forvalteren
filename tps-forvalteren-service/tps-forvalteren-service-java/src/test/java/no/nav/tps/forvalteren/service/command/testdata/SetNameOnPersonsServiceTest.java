package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetNameOnPersonsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SetNameOnPersonsServiceTest {

    private SetNameOnPersonsService command;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    private ArrayList<Person> people;
    private Person personMock1;
    private Person personMock2;

    @Before
    public void before() {
        command = new SetNameOnPersonsService();
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

        assertThat(stringCaptor.getAllValues().get(0), not(isEmptyOrNullString()));
        assertThat(stringCaptor.getAllValues().get(1), not(isEmptyOrNullString()));
    }

    @Test
    public void etternavnTest() {
        command.execute(people);

        verify(personMock1).setEtternavn(stringCaptor.capture());
        verify(personMock2).setEtternavn(stringCaptor.capture());

        assertThat(stringCaptor.getAllValues().get(0), not(isEmptyOrNullString()));
        assertThat(stringCaptor.getAllValues().get(1), not(isEmptyOrNullString()));
    }

}
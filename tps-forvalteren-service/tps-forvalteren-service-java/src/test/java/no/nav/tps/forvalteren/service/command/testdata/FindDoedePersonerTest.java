package no.nav.tps.forvalteren.service.command.testdata;

import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;

@RunWith(MockitoJUnitRunner.class)
public class FindDoedePersonerTest {

    private FindDoedePersoner findDoedePersoner;

    @Mock
    private Person anAlivePersonMock1;
    @Mock
    private Person anAlivePersonMock2;
    @Mock
    private Person aDeadPersonMock1;
    @Mock
    private Person aDeadPersonMock2;

    private List<Person> personer;
    private List<Person> alivePersoner;
    private List<Person> deadPersoner;

    @Before
    public void setup() {
        findDoedePersoner = new FindDoedePersoner();

        LocalDateTime doedsdato = LocalDateTime.now();

        personer = Arrays.asList(anAlivePersonMock1, anAlivePersonMock2, aDeadPersonMock1, aDeadPersonMock2);
        alivePersoner = Arrays.asList(anAlivePersonMock1, anAlivePersonMock2);
        deadPersoner = Arrays.asList(aDeadPersonMock1, aDeadPersonMock2);

        when(anAlivePersonMock1.getDoedsdato()).thenReturn(null);
        when(anAlivePersonMock2.getDoedsdato()).thenReturn(null);
        when(aDeadPersonMock1.getDoedsdato()).thenReturn(doedsdato);
        when(aDeadPersonMock2.getDoedsdato()).thenReturn(doedsdato);
    }

    @Test
    public void findsAllDeadPersons() {
        List<Person> returned;
        returned = findDoedePersoner.execute(personer);

        assertThat(returned, is(deadPersoner));

    }

    @Test
    public void doesNotFindAnyAlivePersons() {
        List<Person> returned;
        returned = findDoedePersoner.execute(personer);

        assertTrue(Collections.disjoint(returned, alivePersoner));
    }

}
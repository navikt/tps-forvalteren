package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindDoedePersoner;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerWithoutDoedsmelding;
import no.nav.tps.forvalteren.service.command.testdata.SaveDoedsmeldingToDB;

@RunWith(MockitoJUnitRunner.class)
public class SendDoedsmeldingTest {

    private Long GRUPPE_ID = 1L;
    private List<String> ENVS = Arrays.asList("u5", "u6");

    private List<Person> personer;
    private List<Person> doedePersoner;
    private List<Person> doedePersonerWithoutDoedsmelding;

    @Mock
    private SkdCreatePersoner skdCreatePersonerMock;

    @Mock
    private FindGruppeById findGruppeByIdMock;

    @Mock
    private FindDoedePersoner findDoedePersonerMock;

    @Mock
    private FindPersonerWithoutDoedsmelding findPersonerWithoutDoedsmeldingMock;

    @Mock
    private SaveDoedsmeldingToDB saveDoedsmeldingToDBMock;

    @InjectMocks
    private SendDoedsmelding sendDoedsmelding;

    @Captor
    private ArgumentCaptor<List<Person>> personCaptor;

    @Before
    public void setup() {
        Gruppe gruppeMock = mock(Gruppe.class);

        Person anAlivePerson = mock(Person.class);
        Person aDeadPersonWithDoedsmelding = mock(Person.class);
        Person aDeadPersonWithoutDoedsmelding = mock(Person.class);

        personer = Arrays.asList(anAlivePerson, aDeadPersonWithDoedsmelding, aDeadPersonWithoutDoedsmelding);
        doedePersoner = Arrays.asList(aDeadPersonWithDoedsmelding, aDeadPersonWithoutDoedsmelding);
        doedePersonerWithoutDoedsmelding = Arrays.asList(aDeadPersonWithoutDoedsmelding);

        when(findGruppeByIdMock.execute(GRUPPE_ID)).thenReturn(gruppeMock);
        when(gruppeMock.getPersoner()).thenReturn(personer);
        when(findDoedePersonerMock.execute(personer)).thenReturn(doedePersoner);
        when(findPersonerWithoutDoedsmeldingMock.execute(doedePersoner)).thenReturn(doedePersonerWithoutDoedsmelding);
    }

    @Test
    public void skdDoedePersonerCalledWithDoedePersonerWithoutDoedsmelding() {
        sendDoedsmelding.execute(GRUPPE_ID, ENVS);

        verify(skdCreatePersonerMock).execute(anyString(), personCaptor.capture(), eq(ENVS));
        assertEquals(personCaptor.getValue(), doedePersonerWithoutDoedsmelding);
    }

    @Test
    public void saveDoedsmeldingToDBCalledWithDoedePersonerWithoutDoedsmelding() {
        sendDoedsmelding.execute(GRUPPE_ID, ENVS);

        verify(saveDoedsmeldingToDBMock).execute(personCaptor.capture());
        assertEquals(personCaptor.getValue(), doedePersonerWithoutDoedsmelding);
    }
}

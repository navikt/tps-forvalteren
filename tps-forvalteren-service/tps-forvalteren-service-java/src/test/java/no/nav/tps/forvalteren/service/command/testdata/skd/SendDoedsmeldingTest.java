package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
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

    private static final Long GRUPPE_ID = 1L;
    private static final Long GRUPPE_ID_NO_DEAD_PERSONS = 2L;
    private static final List<String> ENVS = Arrays.asList("u5", "u6");

    private List<Person> personer;
    private List<Person> doedePersoner;
    private List<Person> alivePersoner;
    private List<Person> doedePersonerWithoutDoedsmelding;

    @Mock
    private SkdMessageSender skdMessageSenderMock;

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
        Gruppe gruppeNoDeadMock = mock(Gruppe.class);

        Person anAlivePerson = mock(Person.class);
        Person aDeadPersonWithDoedsmelding = mock(Person.class);
        Person aDeadPersonWithoutDoedsmelding = mock(Person.class);

        personer = Arrays.asList(anAlivePerson, aDeadPersonWithDoedsmelding, aDeadPersonWithoutDoedsmelding);
        doedePersoner = Arrays.asList(aDeadPersonWithDoedsmelding, aDeadPersonWithoutDoedsmelding);
        alivePersoner = Arrays.asList(anAlivePerson);
        doedePersonerWithoutDoedsmelding = Arrays.asList(aDeadPersonWithoutDoedsmelding);

        when(findGruppeByIdMock.execute(GRUPPE_ID)).thenReturn(gruppeMock);
        when(gruppeMock.getPersoner()).thenReturn(personer);
        when(findDoedePersonerMock.execute(personer)).thenReturn(doedePersoner);
        when(findPersonerWithoutDoedsmeldingMock.execute(doedePersoner)).thenReturn(doedePersonerWithoutDoedsmelding);

        when(findGruppeByIdMock.execute(GRUPPE_ID_NO_DEAD_PERSONS)).thenReturn(gruppeNoDeadMock);
        when(gruppeNoDeadMock.getPersoner()).thenReturn(alivePersoner);
        when(findDoedePersonerMock.execute(alivePersoner)).thenReturn(Collections.emptyList());
        when(findPersonerWithoutDoedsmeldingMock.execute(Collections.emptyList())).thenReturn(Collections.emptyList());
    }

    @Test
    public void skdCreatePersonerCalledWithDoedePersonerWithoutDoedsmelding() {
        sendDoedsmelding.execute(GRUPPE_ID, ENVS);

        verify(skdMessageSenderMock).execute(anyString(), personCaptor.capture(), eq(ENVS), any());
        assertEquals(personCaptor.getValue(), doedePersonerWithoutDoedsmelding);
    }

    @Test
    public void saveDoedsmeldingToDBCalledWithDoedePersonerWithoutDoedsmelding() {
        sendDoedsmelding.execute(GRUPPE_ID, ENVS);

        verify(saveDoedsmeldingToDBMock).execute(personCaptor.capture());
        assertEquals(personCaptor.getValue(), doedePersonerWithoutDoedsmelding);
    }

    @Test
    public void noFurtherCallsWhenNoDoedePersoner() {
        sendDoedsmelding.execute(GRUPPE_ID_NO_DEAD_PERSONS, ENVS);

        verify(skdMessageSenderMock, never()).execute(any(), any(), any(), any());
        verify(saveDoedsmeldingToDBMock, never()).execute(any());
    }
}

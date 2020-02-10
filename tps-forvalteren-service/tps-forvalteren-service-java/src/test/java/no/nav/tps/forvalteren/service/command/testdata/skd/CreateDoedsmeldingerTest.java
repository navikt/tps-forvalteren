package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindDoedePersoner;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerWithoutDoedsmelding;
import no.nav.tps.forvalteren.service.command.testdata.SaveDoedsmeldingToDB;

@RunWith(MockitoJUnitRunner.class)
public class CreateDoedsmeldingerTest {

    private static final Long GRUPPE_ID = 1L;
    private static final Long GRUPPE_ID_NO_DEAD_PERSONS = 2L;
    private static final boolean ADD_HEADER = true;

    private List<Person> personer;
    private List<Person> doedePersoner;
    private List<Person> alivePersoner;
    private List<Person> doedePersonerWithoutDoedsmelding;

    @Mock
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1Mock;

    @Mock
    private FindGruppeById findGruppeByIdMock;

    @Mock
    private FindDoedePersoner findDoedePersonerMock;

    @Mock
    private FindPersonerWithoutDoedsmelding findPersonerWithoutDoedsmeldingMock;

    @Mock
    private SaveDoedsmeldingToDB saveDoedsmeldingToDBMock;

    @InjectMocks
    private CreateDoedsmeldinger createDoedsmeldinger;

    @Captor
    private ArgumentCaptor<List<Person>> personCaptor;

    //TODO Fiks disse testene!

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

        when(findDoedePersonerMock.execute(personer)).thenReturn(doedePersoner);
        when(findPersonerWithoutDoedsmeldingMock.execute(doedePersoner)).thenReturn(doedePersonerWithoutDoedsmelding);

        when(findDoedePersonerMock.execute(alivePersoner)).thenReturn(Collections.emptyList());
        when(findPersonerWithoutDoedsmeldingMock.execute(Collections.emptyList())).thenReturn(Collections.emptyList());
    }

    @Test
    public void skdCreatePersonerCalledWithDoedePersonerWithoutDoedsmelding() {
        createDoedsmeldinger.execute(personer, ADD_HEADER);

        verify(skdMessageCreatorTrans1Mock).execute(anyString(), personCaptor.capture(), eq(ADD_HEADER));
        assertThat(personCaptor.getValue(), is(equalTo(doedePersonerWithoutDoedsmelding)));
    }

    @Test
    public void saveDoedsmeldingToDBCalledWithDoedePersonerWithoutDoedsmelding() {
        createDoedsmeldinger.execute(personer, ADD_HEADER);

        verify(saveDoedsmeldingToDBMock).execute(personCaptor.capture());
        assertThat(personCaptor.getValue(), is(equalTo(doedePersonerWithoutDoedsmelding)));
    }

    @Test
    public void noFurtherCallsWhenNoDoedePersoner() {
        createDoedsmeldinger.execute(alivePersoner, ADD_HEADER);

        verify(skdMessageCreatorTrans1Mock, never()).execute(anyString(), anyList(), anyBoolean());
        verify(saveDoedsmeldingToDBMock, never()).execute(anyList());
    }
}

package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.SaveDoedsmeldingToDB;
import no.nav.tps.forvalteren.service.command.testdata.SjekkDoedsmeldingSentForPerson;

//@RunWith(MockitoJUnitRunner.class)
public class SendDoedsmeldingTest {

//    private Person anAlivePerson;
//    private Person aDeadPersonMedDoedsmelding;
//    private Person aDeadPersonUtenDoedsmelding;
//
//    private LocalDateTime doedsdato;
//    private List<Person> personer;
//    private List<String> environments;
//
//    private Long ALIVE_PERSON_ID = 1L;
//    private Long DEAD_PERSON_MED_DOEDSMELDING_ID = 2L;
//    private Long DEAD_PERSON_UTEN_DOEDSMELDING_ID = 3L;
//
//    @Mock
//    private SkdCreatePersoner skdCreatePersonerMock;
//
//    @Mock
//    private FindGruppeById findGruppeByIdMock;
//
//    @Mock
//    private Gruppe gruppeMock;
//
//    @Mock
//    private SjekkDoedsmeldingSentForPerson sjekkDoedsmeldingSentForPersonMock;
//
//    @Mock
//    private SaveDoedsmeldingToDB saveDoedsmeldingToDBMock;
//
//    @InjectMocks
//    private SendDoedsmelding sendDoedsmelding;
//
//    @Captor
//    private ArgumentCaptor<List<Person>> personCaptor;

//    @Before
//    public void setup() {
//        environments = Collections.singletonList("t");
//        //        environments = Arrays.asList("t");
//
//        anAlivePerson = new Person();
//        aDeadPersonMedDoedsmelding = new Person();
//        aDeadPersonUtenDoedsmelding = new Person();
//
//        anAlivePerson.setId(ALIVE_PERSON_ID);
//        aDeadPersonMedDoedsmelding.setId(DEAD_PERSON_MED_DOEDSMELDING_ID);
//        aDeadPersonUtenDoedsmelding.setId(DEAD_PERSON_UTEN_DOEDSMELDING_ID);
//
//        doedsdato = LocalDateTime.now();
//        aDeadPersonMedDoedsmelding.setDoedsdato(doedsdato);
//
//        personer = new ArrayList<>();
//        personer.add(anAlivePerson);
//        personer.add(aDeadPersonMedDoedsmelding);
//        personer.add(aDeadPersonUtenDoedsmelding);
//
//        when(findGruppeByIdMock.execute(any())).thenReturn(gruppeMock);
//        when(gruppeMock.getPersoner()).thenReturn(personer);
//        when(sjekkDoedsmeldingSentForPersonIdMock.execute(ALIVE_PERSON_ID)).thenReturn(false);
//        when(sjekkDoedsmeldingSentForPersonIdMock.execute(DEAD_PERSON_MED_DOEDSMELDING_ID)).thenReturn(true);
//        when(sjekkDoedsmeldingSentForPersonIdMock.execute(DEAD_PERSON_UTEN_DOEDSMELDING_ID)).thenReturn(false);
//
//    }
//
//    @Test
//    public void doedsmeldingBlirSendtOgLagretVedNyeDoedePersoner() {
//        sendDoedsmelding.execute(any(), any());
//        verify(skdCreatePersonerMock, times(1)).execute(any(), any(), any());
//        verify(saveDoedsmeldingToDBMock, times(1)).execute(any());
//    }
//
//    @Test
//    public void doedsmeldingBlirSendtOgLagretMedKunNyeDoedePersoner() {
//        sendDoedsmelding.execute(any(), any());
//        verify(skdCreatePersonerMock).execute(eq(""), personCaptor.capture(), eq(environments));
//
//        List<Person> personArguments = personCaptor.getValue();
//
//        assertThat(personArguments, hasItem(aDeadPersonMedDoedsmelding));
//        assertThat(personArguments, not(hasItem(anAlivePerson)));
//    }
//
//    @Test
//    public void doedsmeldingBlirIkkeSendtVedIngenDoedePersoner() {
//        List<Person> ingenDoedePersoner = Collections.singletonList(anAlivePerson);
//        when(gruppeMock.getPersoner()).thenReturn(ingenDoedePersoner);
//
//        sendDoedsmelding.execute(any(), any());
//        verify(skdCreatePersonerMock, times(0)).execute(any(), any(), any());
//    }
}

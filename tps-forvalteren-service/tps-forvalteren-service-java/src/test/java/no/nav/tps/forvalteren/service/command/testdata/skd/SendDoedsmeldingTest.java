package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.SaveDoedsmelding;
import no.nav.tps.forvalteren.service.command.testdata.SjekkDoedsmeldingSentForPersonId;

@RunWith(MockitoJUnitRunner.class)
public class SendDoedsmeldingTest {

    private Person anAlivePerson;
    private Person aDeadPerson;
    private LocalDateTime doedsdato;
    private List<Person> personer;

    @Mock
    private SkdCreatePersoner skdCreatePersonerMock;

    @Mock
    private FindGruppeById findGruppeByIdMock;

    @Mock
    private SjekkDoedsmeldingSentForPersonId sjekkDoedsmeldingSentForPersonIdMock;

    @Mock
    private SaveDoedsmelding saveDoedsmeldingMock;

    @InjectMocks
    private SendDoedsmelding sendDoedsmelding;

    @Before
    public void setup() {
        anAlivePerson = new Person();
        aDeadPerson = new Person();

        doedsdato = LocalDateTime.now();

        aDeadPerson.setDoedsdato(doedsdato);

        personer = new ArrayList<>();
        personer.add(anAlivePerson);
        personer.add(aDeadPerson);
    }

    @Test
    public void findDoedePersonerTest() {
    }
}

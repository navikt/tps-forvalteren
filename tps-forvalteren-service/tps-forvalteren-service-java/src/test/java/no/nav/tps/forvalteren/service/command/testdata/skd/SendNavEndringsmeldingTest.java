package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.service.command.testdata.FinnPersonerForNavEndringsmelding;
import no.nav.tps.forvalteren.service.command.testdata.OpprettEgenAnsattMelding;
import no.nav.tps.forvalteren.service.command.testdata.OpprettSikkerhetstiltakMelding;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SendNavEndringsmeldingTest {

    @Mock
    private OpprettEgenAnsattMelding opprettEgenAnsattMelding;

    @Mock
    private OpprettSikkerhetstiltakMelding opprettSikkerhetstiltakMelding;

    @Mock
    private FinnPersonerForNavEndringsmelding finnPersonerForNavEndringsmelding;

    @Mock
    private UserContextHolder userContextHolder;

    @Mock
    private TpsNavEndringsMelding tpsNavEndringsMelding;

    @Mock
    private TpsServiceRoutineRequest tpsServiceRoutineRequest;

    @Mock
    private TpsRequestSender tpsRequestSender;

    @Mock
    private TpsRequestContext tpsRequestContext;

    @Mock
    private TpsServiceRoutineResponse tpsServiceRoutineResponse;

    @InjectMocks
    private SendNavEndringsmelding sendNavEndringsmelding;

    private Person testPerson1;
    private Person testPerson2;

    private Set<String> environments;

    @Before
    public void setup() {
        List<Person> testPersonerListe = new ArrayList<>();
        testPersonerListe.add(testPerson1);
        testPersonerListe.add(testPerson2);

        tpsServiceRoutineRequest = new TpsServiceRoutineRequest();

        environments = new HashSet<String>();
        environments.add("u5");

        when(finnPersonerForNavEndringsmelding.execute(anyListOf(Person.class))).thenReturn(testPersonerListe);
        when(userContextHolder.getUser()).thenReturn(new User("Z111111", "Z111111"));

    }

    @Test
    public void execute() {
        List<TpsNavEndringsMelding> opprettEgenAnsattResultat = new ArrayList<>();
        List<TpsNavEndringsMelding> opprettSikTiltakResultat = new ArrayList<>();

        opprettEgenAnsattResultat.add(new TpsNavEndringsMelding());
        opprettSikTiltakResultat.add(new TpsNavEndringsMelding());

        when(opprettEgenAnsattMelding.execute(testPerson1, environments)).thenReturn(opprettEgenAnsattResultat);
        when(opprettSikkerhetstiltakMelding.execute(testPerson1, environments)).thenReturn(opprettSikTiltakResultat);
        when(tpsNavEndringsMelding.getMelding()).thenReturn(tpsServiceRoutineRequest);

        sendNavEndringsmelding.execute(new ArrayList<Person>(), environments);

        verify(tpsRequestSender, times(4)).sendTpsRequest(any(), any());
    }
}

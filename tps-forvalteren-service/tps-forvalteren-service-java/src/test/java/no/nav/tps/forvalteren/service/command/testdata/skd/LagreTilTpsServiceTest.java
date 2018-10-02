package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerSomSkalHaFoedselsmelding;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;
import no.nav.tps.forvalteren.service.command.testdata.UppercaseDataInPerson;

@RunWith(MockitoJUnitRunner.class)
public class LagreTilTpsServiceTest {

    private static final Long GRUPPE_ID = 1337L;

    @Mock
    private FindPersonsNotInEnvironments findPersonsNotInEnvironments;

    @Mock
    private FindPersonerSomSkalHaFoedselsmelding findPersonerSomSkalHaFoedselsmelding;

    @Mock
    private FindGruppeById findGruppeById;

    @Mock
    private SendNavEndringsmeldinger sendNavEndringsmeldinger;

    @Mock
    private SkdMeldingSender skdMeldingSender;

    @Mock
    private UppercaseDataInPerson uppercaseDataInPerson;

    @InjectMocks
    private LagreTilTpsService lagreTilTpsService;

    private List<Person> persons = new ArrayList<>();
    private List<Person> personsInGruppe = new ArrayList<>();
    private Gruppe gruppe = Gruppe.builder().personer(personsInGruppe).build();
    private Person person = aMalePerson().build();
    private List<String> environments = new ArrayList<>();

    private Map<String, String> expectedStatus = new HashMap<>();
    private Map<String, String> tpsResponse = new HashMap<>();

    {
        persons.add(person);
        environments.add("u2");
        environments.add("env");
        environments.add("env2");
        expectedStatus.put("env", "OK");
        expectedStatus.put("env2", "persistering feilet");
        expectedStatus.put("u2", "Environment is not deployed");
        tpsResponse.put("env", "00");
        tpsResponse.put("env2", "persistering feilet");
        gruppe.getPersoner().add(person);
    }

    // TODO Mangelfull testing her må rettes opp

    @Before
    public void setup() {
        when(findGruppeById.execute(anyLong())).thenReturn(gruppe);
        when(findPersonsNotInEnvironments.execute(personsInGruppe, environments)).thenReturn(persons);
        when(findPersonerSomSkalHaFoedselsmelding.execute(personsInGruppe)).thenReturn(persons);
    }

    @Test
    public void checkThatServicesGetsCalled() {
        lagreTilTpsService.execute(GRUPPE_ID, environments);

        verifyServicesBeingCalled();
    }

    @Test
    public void checkThatServicesGetsCalledAlternateInvocation() {
        lagreTilTpsService.execute(personsInGruppe, environments);

        verifyServicesBeingCalled();
    }

    private void verifyServicesBeingCalled() {
        verify(findPersonsNotInEnvironments).execute(personsInGruppe, environments);
        verify(uppercaseDataInPerson).execute(person);
        verify(findPersonsNotInEnvironments).execute(gruppe.getPersoner(), environments);
        verify(findPersonerSomSkalHaFoedselsmelding).execute(gruppe.getPersoner());

        verify(skdMeldingSender).sendInnvandringsMeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendUpdateInnvandringsMeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendFoedselsMeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendRelasjonsmeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendDoedsmeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendVergemaalsmeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendUtvandringsmeldinger(anyList(), anySet());

        verify(sendNavEndringsmeldinger).execute(anyList(), anySet());
    }
}

package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;

@RunWith(MockitoJUnitRunner.class)
public class LagreTilTpsTest {

    @InjectMocks
    private LagreTilTps lagreTilTps;

    @Mock
    private SkdCreatePersoner skdCreatePersoner;

    @Mock
    private FindPersonsNotInEnvironments findPersonsNotInEnvironments;

    @Mock
    private CreateRelasjoner createRelasjoner;

    @Mock
    private SkdFelterContainerTrans1 skdFelterContainerTrans1;

    private static final String NAVN_INNVANDRINGSMELDING = "Innvandring";
    private List<Person> persons = new ArrayList<>();
    private Person person = aMalePerson().build();
    private List<String> environments = new ArrayList<>();
    private Long gruppeId = 1337L;

    @Before
    public void setup() {
        persons.add(person);
        environments.add("u2");
        when(findPersonsNotInEnvironments.execute(gruppeId, environments)).thenReturn(persons);
        lagreTilTps.execute(gruppeId, environments);
    }

    @Test
    public void checkThatServicesGetsCalled() {
        verify(findPersonsNotInEnvironments).execute(gruppeId, environments);
        verify(skdCreatePersoner).execute(NAVN_INNVANDRINGSMELDING, persons, environments, skdFelterContainerTrans1);
        verify(createRelasjoner).execute(persons, environments);
    }



}
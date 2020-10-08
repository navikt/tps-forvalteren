package no.nav.tps.forvalteren.service.command.testdata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.google.common.collect.Sets;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.test.provider.GruppeProvider;
import no.nav.tps.forvalteren.domain.test.provider.PersonProvider;

@RunWith(MockitoJUnitRunner.class)
public class FindPersonsNotInEnvironmentsTest {

    @InjectMocks
    private FindPersonsNotInEnvironments findPersonsNotInEnvironments;

    @Mock
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;

    private final Long GRUPPE_ID = 1337L;
    private Gruppe gruppe = GruppeProvider.aGruppe().id(GRUPPE_ID).build();
    private Person person = PersonProvider.aMalePerson().build();
    private Person person2 = PersonProvider.aFemalePerson().build();
    private Set<String> environments = Sets.newHashSet("u2", "u6");

    @Before
    public void setup() {
        gruppe.setPersoner(Arrays.asList(person, person2));
    }

    @Test
    public void findNoPersonsInEnvironment() {
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = new HashSet<>();
        identerSomIkkeFinnesiTPSiMiljoe.add(person.getIdent());
        identerSomIkkeFinnesiTPSiMiljoe.add(person2.getIdent());

        when(filtrerPaaIdenterTilgjengeligIMiljo.filtrer(anySet(), anySet())).thenReturn(identerSomIkkeFinnesiTPSiMiljoe);

        List<Person> result = findPersonsNotInEnvironments.execute(Arrays.asList(person, person2), environments);

        assertThat(result, hasSize(2));
        assertThat(result, hasItems(person, person2));
    }

    @Test
    public void allPersonsAreInEnvironments() {

        List<Person> result = findPersonsNotInEnvironments.execute(Arrays.asList(person, person2), environments);
        assertThat(result, hasSize(0));
    }

    @Test
    public void onePersonIsNotInEnvironment() {
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = new HashSet<>();
        identerSomIkkeFinnesiTPSiMiljoe.add(person.getIdent());
        when(filtrerPaaIdenterTilgjengeligIMiljo.filtrer(anySet(), anySet())).thenReturn(identerSomIkkeFinnesiTPSiMiljoe);

        List<Person> result = findPersonsNotInEnvironments.execute(Arrays.asList(person, person2), environments);
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(person));
    }
}
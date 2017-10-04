package no.nav.tps.forvalteren.service.command.testdata;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.test.provider.GruppeProvider;
import no.nav.tps.forvalteren.domain.test.provider.PersonProvider;

@RunWith(MockitoJUnitRunner.class)
public class FindPersonsNotInEnvironmentsTest {

    @InjectMocks
    private FindPersonsNotInEnvironments findPersonsNotInEnvironments;

    @Mock
    private FindGruppeById findGruppeById;

    @Mock
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljo;

    private final Long GRUPPE_ID = 1337L;
    private Gruppe gruppe = GruppeProvider.aGruppe().id(GRUPPE_ID).build();
    private Person person = PersonProvider.aMalePerson().build();
    private Person person2 = PersonProvider.aFemalePerson().build();
    private List<String> environments = new ArrayList<>(Arrays.asList("u2", "u6"));
    private List<String> personIdenter = new ArrayList<>(Arrays.asList(person.getIdent(), person2.getIdent()));

    @Before
    public void setup() {
        gruppe.setPersoner(Arrays.asList(person, person2));
        when(findGruppeById.execute(GRUPPE_ID)).thenReturn(gruppe);
    }

    @Test
    public void findNoPersonsInEnvironment() {
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = new HashSet<>();
        identerSomIkkeFinnesiTPSiMiljoe.add(person.getIdent());
        identerSomIkkeFinnesiTPSiMiljoe.add(person2.getIdent());
        when(filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(personIdenter, new HashSet<>(environments))).thenReturn(identerSomIkkeFinnesiTPSiMiljoe);

        List<Person> result = findPersonsNotInEnvironments.execute(GRUPPE_ID, environments);
        assertThat(result, hasSize(2));
        assertThat(result, hasItems(person, person2));
    }

    @Test
    public void allPersonsAreInEnvironments() {
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = new HashSet<>();
        when(filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(personIdenter, new HashSet<>(environments))).thenReturn(identerSomIkkeFinnesiTPSiMiljoe);

        List<Person> result = findPersonsNotInEnvironments.execute(GRUPPE_ID, environments);
        assertThat(result, hasSize(0));
    }

    @Test
    public void onePersonIsNotInEnvironment() {
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = new HashSet<>();
        identerSomIkkeFinnesiTPSiMiljoe.add(person.getIdent());
        when(filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(personIdenter, new HashSet<>(environments))).thenReturn(identerSomIkkeFinnesiTPSiMiljoe);

        List<Person> result = findPersonsNotInEnvironments.execute(GRUPPE_ID, environments);
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(person));
    }
}
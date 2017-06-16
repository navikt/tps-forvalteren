package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FindIdenterNotUsedInDBTest {

    private static final String dummyIdent1 = "dummy1";
    private static final String dummyIdent2 = "dummy2";
    private static final String dummyIdent3 = "dummy3";
    private final Person person1 = new Person();
    private final Person person2 = new Person();
    private final Person person3 = new Person();

    @Mock
    private PersonRepository personRepositoryMock;

    @InjectMocks
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;

    @Before
    public void setup() {
        person1.setIdent(dummyIdent1);
        person2.setIdent(dummyIdent2);
        person3.setIdent(dummyIdent3);
    }


    @Test
    public void fjernerIdenterFraInputSetSomManFinnerIDB() {
        when(personRepositoryMock.findByIdentIn(any())).thenReturn(Arrays.asList(person1,person2));

        Set<String> identer = new HashSet<>();
        identer.add(dummyIdent1);
        identer.add(dummyIdent2);
        identer.add(dummyIdent3);

        Set<String> identerSet = findIdenterNotUsedInDB.filtrer(identer);

        assertThat(identerSet.contains(dummyIdent3), is(true));
        assertThat(identerSet.contains(dummyIdent2), is(false));
        assertThat(identerSet.contains(dummyIdent1), is(false));
    }

    @Test
    public void hvisIngenIdenterErIDBSaaReturnerersAlleIdenter() {
        when(personRepositoryMock.findByIdentIn(any())).thenReturn(new ArrayList<>());

        Set<String> identer = new HashSet<>();
        identer.add(dummyIdent1);
        identer.add(dummyIdent2);
        identer.add(dummyIdent3);

        Set<String> identerSet = findIdenterNotUsedInDB.filtrer(identer);

        assertThat(identerSet.contains(dummyIdent3), is(true));
        assertThat(identerSet.contains(dummyIdent2), is(true));
        assertThat(identerSet.contains(dummyIdent1), is(true));
    }

    @Test
    public void returnererTomListeHvisAlleErTatt() {
        when(personRepositoryMock.findByIdentIn(any())).thenReturn(Arrays.asList(person1,person2, person3));

        Set<String> identer = new HashSet<>();
        identer.add(dummyIdent1);
        identer.add(dummyIdent2);
        identer.add(dummyIdent3);

        Set<String> identerIkkeIDB = findIdenterNotUsedInDB.filtrer(identer);

        assertThat(identerIkkeIDB.isEmpty(), is(true));
    }

}
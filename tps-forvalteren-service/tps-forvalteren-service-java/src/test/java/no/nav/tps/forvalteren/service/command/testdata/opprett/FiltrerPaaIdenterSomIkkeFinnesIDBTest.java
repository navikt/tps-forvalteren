package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.service.command.testdata.opprett.implementation.DefaultFiltrerPaaIdenterSomIkkeFinnesIDB;
import no.nav.tps.forvalteren.service.command.testdata.opprett.implementation.DefaultFindIdenterNotUsedInDB;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FiltrerPaaIdenterSomIkkeFinnesIDBTest {

    private TestdataRequest testdataRequest1, testdataRequest2;
    private RsPersonKriterier dummyKriterie = new RsPersonKriterier();
    private String dummyIdent1 = "dummy1";
    private String dummyIdent2 = "dummy2";
    private String dummyIdent3 = "dummy3";

    @Mock
    private DefaultFindIdenterNotUsedInDB findIdenterNotUsedInDB;

    @InjectMocks
    private DefaultFiltrerPaaIdenterSomIkkeFinnesIDB filtrerPaaIdenterSomIkkeFinnesIDB;

    @Before
    public void setup(){
//        testdataRequest1 = new TestdataRequest(dummyKriterie);
//        testdataRequest2 = new TestdataRequest(dummyKriterie);
//        testdataRequest1.setIdenterTilgjengligIMiljoe(new HashSet<>());
//        testdataRequest2.setIdenterTilgjengligIMiljoe(new HashSet<>());
    }

    @Test
    public void hvisAlleIdenterFraTestrequestsIkkeErTattIDBSaaReturneresAlleIdenter() {
//
//        testdataRequest1.getIdenterTilgjengligIMiljoe().add(dummyIdent1);
//        testdataRequest1.getIdenterTilgjengligIMiljoe().add(dummyIdent2);
//        testdataRequest2.getIdenterTilgjengligIMiljoe().add(dummyIdent3);
//
//        Set<String> identer = new HashSet<>();
//        identer.add(dummyIdent1);
//        identer.add(dummyIdent2);
//        identer.add(dummyIdent3);
//
//        when(findIdenterNotUsedInDB.filtrer(any())).thenReturn(identer);
//
//        filtrerPaaIdenterSomIkkeFinnesIDB.execute(Arrays.asList(testdataRequest1, testdataRequest2));
//
//        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe()
//                .containsAll(Arrays.asList(dummyIdent1,dummyIdent2)),is(true));
//
//        assertThat(testdataRequest2.getIdenterTilgjengligIMiljoe().contains(dummyIdent3),is(true));
    }

    @Test
    public void hvisEnIdentFraTestrequestsErTattIDBSaaFjernesIdentenFraTestRequesten() {
//
//        testdataRequest1.getIdenterTilgjengligIMiljoe().add(dummyIdent1);
//        testdataRequest1.getIdenterTilgjengligIMiljoe().add(dummyIdent2);
//        testdataRequest2.getIdenterTilgjengligIMiljoe().add(dummyIdent3);
//
//        Set<String> identer = new HashSet<>();
//        identer.add(dummyIdent1);
//        identer.add(dummyIdent3);
//
//        when(findIdenterNotUsedInDB.filtrer(any())).thenReturn(identer);
//
//        filtrerPaaIdenterSomIkkeFinnesIDB.execute(Arrays.asList(testdataRequest1, testdataRequest2));
//
//        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent1),is(true));
//        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent2),is(false));
//        assertThat(testdataRequest2.getIdenterTilgjengligIMiljoe().contains(dummyIdent3),is(true));
    }

    @Test
    public void verifiserAtFindIdenterNotUsedInDbBlirKalltMedAlleIdenterFraTestRequests() {
//        testdataRequest1.getIdenterTilgjengligIMiljoe().add(dummyIdent1);
//        testdataRequest2.getIdenterTilgjengligIMiljoe().add(dummyIdent3);
//
//        Set<String> identer = new HashSet<>();
//        identer.add(dummyIdent1);
//        identer.add(dummyIdent3);
//
//        when(findIdenterNotUsedInDB.filtrer(any())).thenReturn(identer);
//
//        ArgumentCaptor<Set> arg = ArgumentCaptor.forClass(Set.class);
//
//        filtrerPaaIdenterSomIkkeFinnesIDB.execute(Arrays.asList(testdataRequest1, testdataRequest2));
//
//        verify(findIdenterNotUsedInDB).filtrer(arg.capture());
//
//        Set<String> identerSet = arg.getValue();
//
//        assertThat(identerSet.containsAll(Arrays.asList(dummyIdent1,dummyIdent3)), is(true));
    }

}
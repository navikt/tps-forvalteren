package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligeIMiljo;
import no.nav.tps.forvalteren.service.command.testdata.opprett.implementation.DefaultFiltererUtIdenterSomAlleredeFinnesIMiljoe;
import no.nav.tps.forvalteren.service.command.vera.GetEnvironments;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FiltererUtIdenterSomAlleredeFinnesIMiljoeTest {

    private TestdataRequest testdataRequest1, testdataRequest2;
    private RsPersonKriterier dummyKriterie = new RsPersonKriterier();
    private String dummyIdent1 = "dummy1";
    private String dummyIdent2 = "dummy2";
    private String dummyIdent3 = "dummy3";
    private Set<String> environments = new HashSet<>();

    @Mock
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljoMock;

    @Mock
    private GetEnvironments getEnvironmentsCommand;

    @InjectMocks
    private DefaultFiltererUtIdenterSomAlleredeFinnesIMiljoe filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest;

    @Before
    public void setup(){
        testdataRequest1 = new TestdataRequest(dummyKriterie);
        testdataRequest2 = new TestdataRequest(dummyKriterie);

        testdataRequest1.setIdenterTilgjengligIMiljoe(new HashSet<>());
        testdataRequest2.setIdenterTilgjengligIMiljoe(new HashSet<>());

        testdataRequest1.setIdenterGenerertForKriterie(new HashSet<>());
        testdataRequest2.setIdenterGenerertForKriterie(new HashSet<>());

        environments.add("test");
        when(getEnvironmentsCommand.getEnvironmentsFromFasit(anyString())).thenReturn(environments);
    }

    @Test
    public void verifiserAtFilterPaaIdenterFilgjengligIMiljoeBlirKaltMedAlleIdenterFraTestdatarequestsInput() {
        testdataRequest1.getIdenterGenerertForKriterie().add(dummyIdent1);
        testdataRequest1.getIdenterGenerertForKriterie().add(dummyIdent2);
        testdataRequest2.getIdenterGenerertForKriterie().add(dummyIdent3);

        ArgumentCaptor<Set> arg = ArgumentCaptor.forClass(Set.class);

        when(filtrerPaaIdenterTilgjengeligeIMiljoMock.filtrer(any(), any())).thenReturn(new HashSet<>());

        filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest.execute(Arrays.asList(testdataRequest1,testdataRequest2));

        verify(filtrerPaaIdenterTilgjengeligeIMiljoMock).filtrer(arg.capture(), any());

        Set<String> identerSet = arg.getValue();

        assertThat(identerSet.containsAll(Arrays.asList(dummyIdent1,dummyIdent3)), is(true));
    }

    @Test
    public void hvisEnIdentIkkeErTilgjengeligIMiljoeSaaBlirDenTattBortFraTestdataRequest() {
        testdataRequest1.getIdenterGenerertForKriterie().add(dummyIdent1);
        testdataRequest1.getIdenterGenerertForKriterie().add(dummyIdent2);
        testdataRequest2.getIdenterGenerertForKriterie().add(dummyIdent3);

        Set<String> identerTilgjenglig = new HashSet<>();
        identerTilgjenglig.add(dummyIdent1);
        identerTilgjenglig.add(dummyIdent3);

        when(filtrerPaaIdenterTilgjengeligeIMiljoMock.filtrer(any(), any())).thenReturn(identerTilgjenglig);

        filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest.execute(Arrays.asList(testdataRequest1,testdataRequest2));

        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent1), is(true));
        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent2), is(false));
        assertThat(testdataRequest2.getIdenterTilgjengligIMiljoe().contains(dummyIdent3), is(true));
    }

    @Test
    public void hvisAlleIdenterERTilgjengligIMiljoeSaaBlirIngenIdenterTattBort() {
        testdataRequest1.getIdenterGenerertForKriterie().add(dummyIdent1);
        testdataRequest1.getIdenterGenerertForKriterie().add(dummyIdent2);
        testdataRequest2.getIdenterGenerertForKriterie().add(dummyIdent3);

        Set<String> identerTilgjenglig = new HashSet<>();
        identerTilgjenglig.add(dummyIdent1);
        identerTilgjenglig.add(dummyIdent2);
        identerTilgjenglig.add(dummyIdent3);

        when(filtrerPaaIdenterTilgjengeligeIMiljoMock.filtrer(any(), any())).thenReturn(identerTilgjenglig);

        filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest.execute(Arrays.asList(testdataRequest1,testdataRequest2));

        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent1), is(true));
        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent2), is(true));
        assertThat(testdataRequest2.getIdenterTilgjengligIMiljoe().contains(dummyIdent3), is(true));
    }

    @Test
    public void hvisAlleIdenterErTattIMiljoeSaaErAlleIdenterTattBortFraFiktiveIdenter() {
        testdataRequest1.getIdenterGenerertForKriterie().add(dummyIdent1);
        testdataRequest1.getIdenterGenerertForKriterie().add(dummyIdent2);
        testdataRequest2.getIdenterGenerertForKriterie().add(dummyIdent3);

        Set<String> identerTilgjenglig = new HashSet<>();

        when(filtrerPaaIdenterTilgjengeligeIMiljoMock.filtrer(any(), any())).thenReturn(identerTilgjenglig);

        filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest.execute(Arrays.asList(testdataRequest1,testdataRequest2));

        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent1), is(false));
        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent2), is(false));
        assertThat(testdataRequest2.getIdenterTilgjengligIMiljoe().contains(dummyIdent3), is(false));

    }

}
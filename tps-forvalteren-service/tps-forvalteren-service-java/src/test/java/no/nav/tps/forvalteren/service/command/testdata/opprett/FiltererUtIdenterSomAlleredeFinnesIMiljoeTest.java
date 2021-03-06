package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligIMiljo;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;

@RunWith(MockitoJUnitRunner.class)
public class FiltererUtIdenterSomAlleredeFinnesIMiljoeTest {

    private TestdataRequest testdataRequest1, testdataRequest2;
    private RsPersonKriterier dummyInputFraPersonMal = new RsPersonKriterier();
    private String dummyIdent1 = "dummy1";
    private String dummyIdent2 = "dummy2";
    private String dummyIdent3 = "dummy3";
    private Set<String> environments = new HashSet<>();

    @Mock
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljoMock;

    @Mock
    private GetEnvironments getEnvironmentsCommand;

    @InjectMocks
    private FiltererUtIdenterSomAlleredeFinnesIMiljoe filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest;

    @Before
    public void setup() {
        testdataRequest1 = new TestdataRequest(dummyInputFraPersonMal);
        testdataRequest2 = new TestdataRequest(dummyInputFraPersonMal);

        testdataRequest1.setIdenterTilgjengligIMiljoe(new HashSet<>());
        testdataRequest2.setIdenterTilgjengligIMiljoe(new HashSet<>());

        testdataRequest1.setIdenterGenerertForKriteria(new HashSet<>());
        testdataRequest2.setIdenterGenerertForKriteria(new HashSet<>());

        environments.add("test");
    }

    @Test
    public void verifiserAtFilterPaaIdenterFilgjengligIMiljoeBlirKaltMedAlleIdenterFraTestdatarequestsInput() {
        testdataRequest1.getIdenterGenerertForKriteria().add(dummyIdent1);
        testdataRequest1.getIdenterGenerertForKriteria().add(dummyIdent2);
        testdataRequest2.getIdenterGenerertForKriteria().add(dummyIdent3);

        ArgumentCaptor<Set> arg = ArgumentCaptor.forClass(Set.class);

        when(filtrerPaaIdenterTilgjengeligIMiljoMock.filtrer(any(), any())).thenReturn(new HashSet<>());

        filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest.executeMotProdliktMiljoe(Arrays.asList(testdataRequest1, testdataRequest2));

        verify(filtrerPaaIdenterTilgjengeligIMiljoMock).filtrer(arg.capture(), any());

        Set<String> identerSet = arg.getValue();

        assertThat(identerSet.containsAll(Arrays.asList(dummyIdent1, dummyIdent3)), is(true));
    }

    @Test
    public void hvisEnIdentIkkeErTilgjengeligIMiljoeSaaBlirDenTattBortFraTestdataRequest() {
        testdataRequest1.getIdenterGenerertForKriteria().add(dummyIdent1);
        testdataRequest1.getIdenterGenerertForKriteria().add(dummyIdent2);
        testdataRequest2.getIdenterGenerertForKriteria().add(dummyIdent3);

        Set<String> identerTilgjenglig = new HashSet<>();
        identerTilgjenglig.add(dummyIdent1);
        identerTilgjenglig.add(dummyIdent3);

        when(filtrerPaaIdenterTilgjengeligIMiljoMock.filtrer(any(), any())).thenReturn(identerTilgjenglig);

        filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest.executeMotProdliktMiljoe(Arrays.asList(testdataRequest1, testdataRequest2));

        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent1), is(true));
        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent2), is(false));
        assertThat(testdataRequest2.getIdenterTilgjengligIMiljoe().contains(dummyIdent3), is(true));
    }

    @Test
    public void hvisAlleIdenterERTilgjengligIMiljoeSaaBlirIngenIdenterTattBort() {
        testdataRequest1.getIdenterGenerertForKriteria().add(dummyIdent1);
        testdataRequest1.getIdenterGenerertForKriteria().add(dummyIdent2);
        testdataRequest2.getIdenterGenerertForKriteria().add(dummyIdent3);

        Set<String> identerTilgjenglig = new HashSet<>();
        identerTilgjenglig.add(dummyIdent1);
        identerTilgjenglig.add(dummyIdent2);
        identerTilgjenglig.add(dummyIdent3);

        when(filtrerPaaIdenterTilgjengeligIMiljoMock.filtrer(any(), any())).thenReturn(identerTilgjenglig);

        filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest.executeMotProdliktMiljoe(Arrays.asList(testdataRequest1, testdataRequest2));

        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent1), is(true));
        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent2), is(true));
        assertThat(testdataRequest2.getIdenterTilgjengligIMiljoe().contains(dummyIdent3), is(true));
    }

    @Test
    public void hvisAlleIdenterErTattIMiljoeSaaErAlleIdenterTattBortFraFiktiveIdenter() {
        testdataRequest1.getIdenterGenerertForKriteria().add(dummyIdent1);
        testdataRequest1.getIdenterGenerertForKriteria().add(dummyIdent2);
        testdataRequest2.getIdenterGenerertForKriteria().add(dummyIdent3);

        Set<String> identerTilgjenglig = new HashSet<>();

        when(filtrerPaaIdenterTilgjengeligIMiljoMock.filtrer(any(), any())).thenReturn(identerTilgjenglig);

        filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest.executeMotProdliktMiljoe(Arrays.asList(testdataRequest1, testdataRequest2));

        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent1), is(false));
        assertThat(testdataRequest1.getIdenterTilgjengligIMiljoe().contains(dummyIdent2), is(false));
        assertThat(testdataRequest2.getIdenterTilgjengligIMiljoe().contains(dummyIdent3), is(false));
    }
}
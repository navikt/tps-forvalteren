package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;

@RunWith(MockitoJUnitRunner.class)
public class TaBortOverfloedigIdenterITestdataRequestTest {

    private TestdataRequest testdataRequest;
    private RsPersonKriterier dummyInputFraKriterier = new RsPersonKriterier();
    private String dummyIdent1 = "dummy1";
    private String dummyIdent2 = "dummy2";
    private String dummyIdent3 = "dummy3";

    @InjectMocks
    private TaBortOverfloedigIdenterITestdataRequest taBortOverfloedigIdenterITestdataRequest;

    @Before
    public void setup() {
        testdataRequest = new TestdataRequest(dummyInputFraKriterier);
        testdataRequest.setIdenterTilgjengligIMiljoe(new HashSet<>());
        testdataRequest.getIdenterTilgjengligIMiljoe().addAll(Arrays.asList(dummyIdent1, dummyIdent2, dummyIdent3));
    }

    @Test
    public void hvisManHarFlereTilgjengeligeEnnNoedvendigFraKritereSaFjernesOverfloedig() {
        dummyInputFraKriterier.setAntall(2);

        taBortOverfloedigIdenterITestdataRequest.execute(testdataRequest);

        assertThat(testdataRequest.getIdenterTilgjengligIMiljoe()
                .containsAll(Arrays.asList(dummyIdent1, dummyIdent2)), is(true));

        assertThat(testdataRequest.getIdenterTilgjengligIMiljoe()
                .contains(dummyIdent3), is(false));
    }

    @Test
    public void hvisManHarLikeMangeIdenterSomKritereTrengerSaaReturneresAlle() {
        dummyInputFraKriterier.setAntall(3);

        taBortOverfloedigIdenterITestdataRequest.execute(testdataRequest);

        assertThat(testdataRequest.getIdenterTilgjengligIMiljoe()
                .containsAll(Arrays.asList(dummyIdent1, dummyIdent2, dummyIdent3)), is(true));
    }

    @Test
    public void hvisManHarFaerreIdenterEnnKritereTrengerSaaReturneresAlle() {
        dummyInputFraKriterier.setAntall(5);

        taBortOverfloedigIdenterITestdataRequest.execute(testdataRequest);

        assertThat(testdataRequest.getIdenterTilgjengligIMiljoe()
                .containsAll(Arrays.asList(dummyIdent1, dummyIdent2, dummyIdent3)), is(true));
    }

}
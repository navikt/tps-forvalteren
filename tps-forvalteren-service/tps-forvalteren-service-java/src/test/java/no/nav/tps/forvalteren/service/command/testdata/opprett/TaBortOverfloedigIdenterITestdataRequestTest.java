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

import no.nav.tps.forvalteren.domain.rs.RsPersonMal;

@RunWith(MockitoJUnitRunner.class)
public class TaBortOverfloedigIdenterITestdataRequestTest {

    private TestdataRequest testdataRequest;
    private RsPersonMal dummyInputFraPersonMal = new RsPersonMal();
    private String dummyIdent1 = "dummy1";
    private String dummyIdent2 = "dummy2";
    private String dummyIdent3 = "dummy3";

    @InjectMocks
    private TaBortOverfloedigIdenterITestdataRequest taBortOverfloedigIdenterITestdataRequest;

    @Before
    public void setup() {
        testdataRequest = new TestdataRequest(dummyInputFraPersonMal);
        testdataRequest.setIdenterTilgjengligIMiljoe(new HashSet<>());
    }

    @Test
    public void hvisManHarFlereTilgjengeligeEnnNoedvendigFraKritereSaFjernesOverfloedig() {
        dummyInputFraPersonMal.setAntallIdenter(2);
        testdataRequest.getIdenterTilgjengligIMiljoe().add(dummyIdent1);
        testdataRequest.getIdenterTilgjengligIMiljoe().add(dummyIdent2);
        testdataRequest.getIdenterTilgjengligIMiljoe().add(dummyIdent3);

        taBortOverfloedigIdenterITestdataRequest.execute(testdataRequest);

        assertThat(testdataRequest.getIdenterTilgjengligIMiljoe()
                .containsAll(Arrays.asList(dummyIdent1, dummyIdent2)), is(true));

        assertThat(testdataRequest.getIdenterTilgjengligIMiljoe()
                .contains(dummyIdent3), is(false));
    }

    @Test
    public void hvisManHarLikeMangeIdenterSomKritereTrengerSaaReturneresAlle() {
        dummyInputFraPersonMal.setAntallIdenter(3);
        testdataRequest.getIdenterTilgjengligIMiljoe().add(dummyIdent1);
        testdataRequest.getIdenterTilgjengligIMiljoe().add(dummyIdent2);
        testdataRequest.getIdenterTilgjengligIMiljoe().add(dummyIdent3);

        taBortOverfloedigIdenterITestdataRequest.execute(testdataRequest);

        assertThat(testdataRequest.getIdenterTilgjengligIMiljoe()
                .containsAll(Arrays.asList(dummyIdent1, dummyIdent2, dummyIdent3)), is(true));
    }

    @Test
    public void hvisManHarFaerreIdenterEnnKritereTrengerSaaReturneresAlle() {
        dummyInputFraPersonMal.setAntallIdenter(5);
        testdataRequest.getIdenterTilgjengligIMiljoe().add(dummyIdent1);
        testdataRequest.getIdenterTilgjengligIMiljoe().add(dummyIdent2);
        testdataRequest.getIdenterTilgjengligIMiljoe().add(dummyIdent3);

        taBortOverfloedigIdenterITestdataRequest.execute(testdataRequest);

        assertThat(testdataRequest.getIdenterTilgjengligIMiljoe()
                .containsAll(Arrays.asList(dummyIdent1, dummyIdent2, dummyIdent3)), is(true));
    }

}
package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.service.command.exceptions.HttpCantSatisfyRequestException;

@RunWith(MockitoJUnitRunner.class)
public class TestdataIdenterFetcherTest {

    private RsPersonKriteriumRequest rsPersonKriteriumRequest;
    private RsPersonKriterier personKriterier1 = new RsPersonKriterier();
    private RsPersonKriterier personKriterier2 = new RsPersonKriterier();
    private RsPersonKriterier personKriterier3 = new RsPersonKriterier();
    private RsPersonKriterier personKriterier4 = new RsPersonKriterier();

    private TestdataRequest testdataRequest1;
    private TestdataRequest testdataRequest2;
    private TestdataRequest testdataRequest3;
    private TestdataRequest testdataRequest4;

    private String dummyIdent1 = "dummy1";
    private String dummyIdent2 = "dummy2";
    private String dummyIdent3 = "dummy3";
    private String dummyIdent4 = "dummy4";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private MessageProvider messageProvider;

    @Mock
    private Testdata testdata;

    @InjectMocks
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Before
    public void setup() {
        rsPersonKriteriumRequest = new RsPersonKriteriumRequest();
        rsPersonKriteriumRequest.setPersonKriterierListe(Arrays.asList(personKriterier1, personKriterier2));

        testdataRequest1 = new TestdataRequest(personKriterier1);
        testdataRequest2 = new TestdataRequest(personKriterier2);
        testdataRequest3 = new TestdataRequest(personKriterier3);
        testdataRequest4 = new TestdataRequest(personKriterier4);
    }

    @Test
    public void hvisAlleKriterierErOppfyltSaaKAllesIkkeMetodeForAaOppdatereForManglendeIdenterForKriterier() {
        personKriterier1.setAntall(1);
        personKriterier2.setAntall(1);

        testdataRequest1.getIdenterTilgjengligIMiljoe().add(dummyIdent1);
        testdataRequest2.getIdenterTilgjengligIMiljoe().add(dummyIdent2);

        when(testdata.genererIdenterForTestdataRequests(any(RsPersonKriteriumRequest.class))).thenReturn(Arrays.asList(testdataRequest1, testdataRequest2));

        testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(rsPersonKriteriumRequest);

        verify(testdata, times(1)).genererIdenterForTestdataRequests(any(RsPersonKriteriumRequest.class));
    }

    @Test
    public void hvisIkkeAlleKriterierErOppfyltVedForsteForskSaaOppdatererManRequestForMangelendeIdenter() {

        personKriterier1.setAntall(1);
        personKriterier2.setAntall(3);

        testdataRequest1.getIdenterTilgjengligIMiljoe().add(dummyIdent1);
        testdataRequest2.getIdenterTilgjengligIMiljoe().add(dummyIdent2);
        testdataRequest3.getIdenterTilgjengligIMiljoe().add(dummyIdent3);
        testdataRequest4.getIdenterTilgjengligIMiljoe().add(dummyIdent4);

        when(testdata.genererIdenterForTestdataRequests(any(RsPersonKriteriumRequest.class)))
                .thenReturn(
                        Arrays.asList(testdataRequest1, testdataRequest2),
                        Arrays.asList(testdataRequest3),
                        Arrays.asList(testdataRequest4)
                );

        List<TestdataRequest> requests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(rsPersonKriteriumRequest);

        verify(testdata, times(3)).genererIdenterForTestdataRequests(any(RsPersonKriteriumRequest.class));

        assertThat(requests.get(1).getIdenterTilgjengligIMiljoe()
                .containsAll(Arrays.asList(dummyIdent2, dummyIdent3, dummyIdent4)), is(true));

    }

    @Test
    public void hvisManIkkeFinnerIdenterSomOppfyllerKriterierEtterMaxAntallForsoekSaaKastException() throws Exception {
        personKriterier1.setAntall(2);

        testdataRequest1.getIdenterTilgjengligIMiljoe().add(dummyIdent1);

        when(testdata.genererIdenterForTestdataRequests(any(RsPersonKriteriumRequest.class))).thenReturn(Arrays.asList(testdataRequest1));
        when(messageProvider.get(anyString())).thenReturn("msg");

        expectedException.expect(HttpCantSatisfyRequestException.class);

        testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(rsPersonKriteriumRequest);
    }
}
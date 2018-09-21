package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
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
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.domain.rs.RsPersonMalRequest;
import no.nav.tps.forvalteren.service.command.exceptions.HttpCantSatisfyRequestException;

@RunWith(MockitoJUnitRunner.class)
public class TestdataIdenterFetcherTest {

    private RsPersonMalRequest inputPersonMalRequest;
    private RsPersonMal dummyInputFraPersonMal1 = new RsPersonMal();
    private RsPersonMal dummyInputFraPersonMal2 = new RsPersonMal();
    private RsPersonMal dummyInputFraPersonMal3 = new RsPersonMal();
    private RsPersonMal dummyInputFraPersonMal4 = new RsPersonMal();

    private TestdataRequest testdataRequest1, testdataRequest2, testdataRequest3, testdataRequest4;

    private String dummyIdent1 = "dummy1";
    private String dummyIdent2 = "dummy2";
    private String dummyIdent3 = "dummy3";
    private String dummyIdent4 = "dummy4";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private MessageProvider messageProviderMock;

    @Mock
    private Testdata testdataMock;

    @InjectMocks
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Before
    public void setup() {
        inputPersonMalRequest = new RsPersonMalRequest();
        inputPersonMalRequest.setInputPersonMalRequest(Arrays.asList(dummyInputFraPersonMal1, dummyInputFraPersonMal2));

        testdataRequest1 = new TestdataRequest(dummyInputFraPersonMal1);
        testdataRequest2 = new TestdataRequest(dummyInputFraPersonMal2);
        testdataRequest3 = new TestdataRequest(dummyInputFraPersonMal3);
        testdataRequest4 = new TestdataRequest(dummyInputFraPersonMal4);

        testdataRequest1.setIdenterTilgjengligIMiljoe(new HashSet<>());
        testdataRequest2.setIdenterTilgjengligIMiljoe(new HashSet<>());
        testdataRequest3.setIdenterTilgjengligIMiljoe(new HashSet<>());
        testdataRequest4.setIdenterTilgjengligIMiljoe(new HashSet<>());

        testdataRequest1.setIdenterGenerertForKriterie(new HashSet<>());
        testdataRequest2.setIdenterGenerertForKriterie(new HashSet<>());
        testdataRequest3.setIdenterGenerertForKriterie(new HashSet<>());
        testdataRequest4.setIdenterGenerertForKriterie(new HashSet<>());
    }

    @Test
    public void hvisAlleKriterierErOppfyltSaaKAllesIkkeMetodeForAaOppdatereForManglendeIdenterForKriterier() {
        dummyInputFraPersonMal1.setAntallIdenter(1);
        dummyInputFraPersonMal2.setAntallIdenter(1);

        testdataRequest1.getIdenterTilgjengligIMiljoe().add(dummyIdent1);
        testdataRequest2.getIdenterTilgjengligIMiljoe().add(dummyIdent2);

        when(testdataMock.genererIdenterForTestdataRequests(any())).thenReturn(Arrays.asList(testdataRequest1, testdataRequest2));

        testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(inputPersonMalRequest);

        verify(testdataMock, times(1)).genererIdenterForTestdataRequests(any());
    }

    @Test
    public void hvisIkkeAlleKriterierErOppfyltVedForsteForskSaaOppdatererManRequestForMangelendeIdenter() {

        dummyInputFraPersonMal1.setAntallIdenter(1);
        dummyInputFraPersonMal2.setAntallIdenter(3);

        testdataRequest1.getIdenterTilgjengligIMiljoe().add(dummyIdent1);
        testdataRequest2.getIdenterTilgjengligIMiljoe().add(dummyIdent2);
        testdataRequest3.getIdenterTilgjengligIMiljoe().add(dummyIdent3);
        testdataRequest4.getIdenterTilgjengligIMiljoe().add(dummyIdent4);

        when(testdataMock.genererIdenterForTestdataRequests(any()))
                .thenReturn(
                        Arrays.asList(testdataRequest1, testdataRequest2),
                        Arrays.asList(testdataRequest3),
                        Arrays.asList(testdataRequest4)
                );

        List<TestdataRequest> requests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(inputPersonMalRequest);

        verify(testdataMock, times(3)).genererIdenterForTestdataRequests(any());

        assertThat(requests.get(1).getIdenterTilgjengligIMiljoe()
                .containsAll(Arrays.asList(dummyIdent2, dummyIdent3, dummyIdent4)), is(true));

    }

    @Test
    public void hvisManIkkeFinnerIdenterSomOppfyllerKriterierEtterMaxAntallForsoekSaaKastException() throws Exception {
        dummyInputFraPersonMal1.setAntallIdenter(2);

        testdataRequest1.getIdenterTilgjengligIMiljoe().add(dummyIdent1);

        when(testdataMock.genererIdenterForTestdataRequests(any())).thenReturn(Arrays.asList(testdataRequest1));
        when(messageProviderMock.get(anyString())).thenReturn("msg");

        expectedException.expect(HttpCantSatisfyRequestException.class);

        testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(inputPersonMalRequest);
    }
}
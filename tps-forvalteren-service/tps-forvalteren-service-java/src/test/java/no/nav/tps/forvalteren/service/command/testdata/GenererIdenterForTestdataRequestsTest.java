package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.service.command.testdata.opprett.implementation.DefaultGenererIdenterForTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GenererIdenterForTestdataRequestsTest {

//    private RsPersonKriteriumRequest rsPersonKriteriumRequest;
//    private RsPersonKriterier personKriterier1 = new RsPersonKriterier();
//    private RsPersonKriterier personKriterier2 = new RsPersonKriterier();

    private String dummyIdent1 = "dummy1";
    private String dummyIdent2 = "dummy2";
    private String dummyIdent3 = "dummy3";

    @Mock
    private FiktiveIdenterGenerator fiktiveIdenterGeneratorMock;

    @InjectMocks
    private DefaultGenererIdenterForTestdataRequests genererIdenterForTestdataRequests;

    @Before
    public void setup() {
//        rsPersonKriteriumRequest = new RsPersonKriteriumRequest();
//        rsPersonKriteriumRequest.setPersonKriterierListe(Arrays.asList(personKriterier1,personKriterier2));
    }

    @Test
    public void hvisManHarFlereKriterierOpprettesIdenterForAlleKritiene() {
//        Set<String> dummyIdenter1 = new HashSet<>();
//        dummyIdenter1.add(dummyIdent1);
//
//        Set<String> dummyIdenter2 = new HashSet<>();
//        dummyIdenter2.add(dummyIdent2);
//
//        when(fiktiveIdenterGeneratorMock.genererFiktiveIdenter(any()))
//                .thenReturn(dummyIdenter1,dummyIdenter2);
//
//        List<TestdataRequest> requests = genererIdenterForTestdataRequests.execute(rsPersonKriteriumRequest);
//
//        assertThat(requests.get(0).getIdenterGenerertForKriterie().contains(dummyIdent1), is(true));
//        assertThat(requests.get(1).getIdenterGenerertForKriterie().contains(dummyIdent2), is(true));
    }

    @Test
    public void hvisManFaarGenerertEnLikIdentForToForskjelligeTestdataRequestsSaaVilIdentBliTattBortFraDenEne() {
//        Set<String> dummyIdenter1 = new HashSet<>();
//        dummyIdenter1.add(dummyIdent1);
//        dummyIdenter1.add(dummyIdent2);
//
//        Set<String> dummyIdenter2 = new HashSet<>();
//        dummyIdenter2.add(dummyIdent2);
//        dummyIdenter2.add(dummyIdent3);
//
//        when(fiktiveIdenterGeneratorMock.genererFiktiveIdenter(any()))
//                .thenReturn(dummyIdenter1,dummyIdenter2);
//
//        List<TestdataRequest> requests = genererIdenterForTestdataRequests.execute(rsPersonKriteriumRequest);
//
//        assertThat(requests.get(0).getIdenterGenerertForKriterie()
//                .containsAll(Arrays.asList(dummyIdent1, dummyIdent2)), is(true));
//
//        assertThat(requests.get(1).getIdenterGenerertForKriterie().contains(dummyIdent2), is(false));
//        assertThat(requests.get(1).getIdenterGenerertForKriterie().contains(dummyIdent3), is(true));
//        assertThat(requests.get(1).getIdenterGenerertForKriterie(), hasSize(1));
    }
}
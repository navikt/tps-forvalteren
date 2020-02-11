package no.nav.tps.forvalteren.service.command.testdata;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.google.common.collect.Sets;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.GenererIdenterForTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;

@RunWith(MockitoJUnitRunner.class)
public class GenererIdenterForTestdataRequestsTest {

    private RsPersonKriteriumRequest rsPersonKriteriumRequest;
    private RsPersonKriterier inputPerson1 = new RsPersonKriterier();
    private RsPersonKriterier inputPerson2 = new RsPersonKriterier();

    private String dummyIdent1 = "dummy1";
    private String dummyIdent2 = "dummy2";
    private String dummyIdent3 = "dummy3";

    @Mock
    private FiktiveIdenterGenerator fiktiveIdenterGeneratorMock;

    @InjectMocks
    private GenererIdenterForTestdataRequests genererIdenterForTestdataRequests;

    @Before
    public void setup() {
        rsPersonKriteriumRequest = new RsPersonKriteriumRequest();
        rsPersonKriteriumRequest.setPersonKriterierListe(Arrays.asList(inputPerson1, inputPerson1));
    }

    @Test
    public void hvisManHarFlereKriterierOpprettesIdenterForAlleKritiene() {
        Set<String> dummyIdenter1 = Sets.newHashSet(dummyIdent1);

        Set<String> dummyIdenter2 = Sets.newHashSet(dummyIdent2);

        when(fiktiveIdenterGeneratorMock.genererFiktiveIdenter(any(RsPersonKriterier.class)))
                .thenReturn(dummyIdenter1, dummyIdenter2);

        List<TestdataRequest> requests = genererIdenterForTestdataRequests.execute(rsPersonKriteriumRequest);

        assertThat(requests.get(0).getIdenterGenerertForKriteria().contains(dummyIdent1), is(true));
        assertThat(requests.get(1).getIdenterGenerertForKriteria().contains(dummyIdent2), is(true));
    }

    @Test
    public void hvisManFaarGenerertEnLikIdentForToForskjelligeTestdataRequestsSaaVilIdentBliTattBortFraDenEne() {
        Set<String> dummyIdenter1 = Sets.newHashSet(dummyIdent1, dummyIdent2);
        Set<String> dummyIdenter2 = Sets.newHashSet(dummyIdent2, dummyIdent3);

        when(fiktiveIdenterGeneratorMock.genererFiktiveIdenter(any(RsPersonKriterier.class)))
                .thenReturn(dummyIdenter1, dummyIdenter2);

        List<TestdataRequest> requests = genererIdenterForTestdataRequests.execute(rsPersonKriteriumRequest);

        assertThat(requests.get(0).getIdenterGenerertForKriteria()
                .containsAll(Arrays.asList(dummyIdent1, dummyIdent2)), is(true));

        assertThat(requests.get(1).getIdenterGenerertForKriteria().contains(dummyIdent2), is(false));
        assertThat(requests.get(1).getIdenterGenerertForKriteria().contains(dummyIdent3), is(true));
        assertThat(requests.get(1).getIdenterGenerertForKriteria(), hasSize(1));
    }
}
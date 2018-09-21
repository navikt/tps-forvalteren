package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.service.command.testdata.FiktiveIdenterGenerator;

@Service
public class GenererIdenterForTestdataRequests {

    @Autowired
    private FiktiveIdenterGenerator fiktiveIdenterGenerator;

    public List<TestdataRequest> execute(RsPersonKriteriumRequest personKriterierRequest) {
        List<TestdataRequest> requests = new ArrayList<>();
        for (RsPersonKriterier kriterie : personKriterierRequest.getPersonKriterierListe()) {
            TestdataRequest request = new TestdataRequest(kriterie);
            request.setIdenterGenerertForKriterie(fiktiveIdenterGenerator.genererFiktiveIdenter(kriterie));
            taBortIdenterLagtTilIAndreKriterier(requests, request.getIdenterGenerertForKriterie());
            requests.add(request);
        }
        return requests;
    }

    private void taBortIdenterLagtTilIAndreKriterier(List<TestdataRequest> testdataRequests, Set<String> identerForKritere) {
        for (String ident : new ArrayList<>(identerForKritere)) {
            for (TestdataRequest testdataRequest : testdataRequests) {
                if (testdataRequest.getIdenterGenerertForKriterie().contains(ident)) {
                    identerForKritere.remove(ident);
                }
            }
        }
    }
}

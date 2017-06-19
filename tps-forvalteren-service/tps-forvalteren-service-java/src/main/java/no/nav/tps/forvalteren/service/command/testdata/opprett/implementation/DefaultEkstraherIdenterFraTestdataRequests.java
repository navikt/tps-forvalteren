package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultEkstraherIdenterFraTestdataRequests implements EkstraherIdenterFraTestdataRequests {

    public List<String> execute(List<TestdataRequest> testdataRequests){
        List<String> identer = new ArrayList<>();
        for(TestdataRequest request : testdataRequests){
            identer.addAll(request.getIdenterTilgjengligIMiljoe());
        }
        return identer;
    }
}

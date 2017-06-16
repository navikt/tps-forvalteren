package no.nav.tps.forvalteren.service.command.testdata.opprett;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EkstraherIdenterFraTestdataRequests {

    public List<String> execute(List<TestdataRequest> testdataRequests){
        List<String> identer = new ArrayList<>();
        for(TestdataRequest request : testdataRequests){
            identer.addAll(request.getIdenterTilgjengligIMiljoe());
        }
        return identer;
    }
}

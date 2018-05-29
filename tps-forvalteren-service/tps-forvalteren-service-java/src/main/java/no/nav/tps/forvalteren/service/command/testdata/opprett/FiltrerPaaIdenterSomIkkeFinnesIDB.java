package no.nav.tps.forvalteren.service.command.testdata.opprett;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FiltrerPaaIdenterSomIkkeFinnesIDB {

    @Autowired
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;

    public void execute(List<TestdataRequest> testdataRequests){
        Set<String> alleGenererteIdenter = new HashSet<>();
        for (TestdataRequest request : testdataRequests) {
            alleGenererteIdenter.addAll(request.getIdenterTilgjengligIMiljoe());
        }
        Set<String> alleTilgjengeligIdenter = findIdenterNotUsedInDB.filtrer(alleGenererteIdenter);
        taBortOpptatteIdenterFraRequest(testdataRequests, alleTilgjengeligIdenter);
    }

    private void taBortOpptatteIdenterFraRequest(List<TestdataRequest> testdataRequests, Set<String> alleTilgjengligIdenter) {
        for (TestdataRequest testdataRequest : testdataRequests) {
            testdataRequest.getIdenterTilgjengligIMiljoe().retainAll(alleTilgjengligIdenter);
        }
    }
}

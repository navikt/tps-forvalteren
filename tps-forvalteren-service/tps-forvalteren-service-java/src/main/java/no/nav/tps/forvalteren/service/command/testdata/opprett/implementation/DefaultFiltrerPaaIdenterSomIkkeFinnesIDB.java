package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.FiltrerPaaIdenterSomIkkeFinnesIDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DefaultFiltrerPaaIdenterSomIkkeFinnesIDB implements FiltrerPaaIdenterSomIkkeFinnesIDB{

    @Autowired
    private DefaultFindIdenterNotUsedInDB findIdenterNotUsedInDB;

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

package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;

import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligIMiljo;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;

@Service
public class FiltererUtIdenterSomAlleredeFinnesIMiljoe {

    @Autowired
    private GetEnvironments getEnvironmentsCommand;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;

    public void executeMotProduliktMiljoe(List<TestdataRequest> testdataRequests) {
        Set<String> alleGenererteIdenter = getAlleGenererteIdenter(testdataRequests);

        // Environment q0 only verified for existence
        Set<String> environments = Sets.newHashSet("q0");
        Set<String> alleTilgjengeligIdenter = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(alleGenererteIdenter, environments);
        taBortOpptatteIdenterRequest(testdataRequests, alleTilgjengeligIdenter);
    }

    public void executeMotAlleMiljoer(List<TestdataRequest> testdataRequests, List<String> miljoer) {
        Set<String> alleGenererteIdenter = getAlleGenererteIdenter(testdataRequests);

        Set<String> alleTilgjengeligIdenter = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(alleGenererteIdenter,
                miljoer != null ? Sets.newHashSet(miljoer) : getEnvironmentsCommand.getEnvironmentsFromFasit("tpsws"));
        taBortOpptatteIdenterRequest(testdataRequests, alleTilgjengeligIdenter);
    }

    private Set<String> getAlleGenererteIdenter(List<TestdataRequest> testdataRequests) {
        Set<String> alleGenererteIdenter = new HashSet<>();
        for (TestdataRequest request : testdataRequests) {
            alleGenererteIdenter.addAll(request.getIdenterGenerertForKriteria());
        }
        return alleGenererteIdenter;
    }

    private void taBortOpptatteIdenterRequest(List<TestdataRequest> testdataRequests, Set<String> alleTilgjengligIdenterIMiljoe) {
        for (TestdataRequest request : testdataRequests) {
            request.setIdenterTilgjengligIMiljoe(new HashSet<>(request.getIdenterGenerertForKriteria()));
            request.getIdenterTilgjengligIMiljoe().retainAll(alleTilgjengligIdenterIMiljoe);
        }
    }
}

package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Objects.nonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligIMiljo;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;

@Service
public class FiltererUtIdenterSomAlleredeFinnesIMiljoe {

    private static final String PRODLIKE_ENV = "q0";

    @Autowired
    private GetEnvironments getEnvironmentsCommand;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;

    public void executeMotProdliktMiljoe(List<TestdataRequest> testdataRequests) {
        Set<String> alleGenererteIdenter = getAlleGenererteIdenter(testdataRequests);

        // Environment PRODLIKE only verified for existence
        Set<String> environments = newHashSet(PRODLIKE_ENV);
        Set<String> alleTilgjengeligIdenter = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(alleGenererteIdenter, environments);
        taBortOpptatteIdenterRequest(testdataRequests, alleTilgjengeligIdenter);
    }

    public void executeMotAlleMiljoer(List<TestdataRequest> testdataRequests, Set<String> miljoer) {
        Set<String> alleGenererteIdenter = getAlleGenererteIdenter(testdataRequests);

        Set<String> alleTilgjengeligIdenter = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(alleGenererteIdenter,
                nonNull(miljoer) && !miljoer.isEmpty() ? miljoer : getEnvironmentsCommand.getEnvironmentsFromFasit("tpsws"));
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

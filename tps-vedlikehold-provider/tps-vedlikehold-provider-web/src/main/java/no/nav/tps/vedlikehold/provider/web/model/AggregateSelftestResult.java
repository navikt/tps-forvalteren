package no.nav.tps.vedlikehold.provider.web.model;

import java.util.ArrayList;
import java.util.List;

import static no.nav.tps.vedlikehold.provider.web.model.SelftestResult.Status.FEILET;
import static no.nav.tps.vedlikehold.provider.web.model.SelftestResult.Status.OK;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class AggregateSelftestResult {
    private List<SelftestResult> results;

    public AggregateSelftestResult() {
        results = new ArrayList<>();
    }

    public SelftestResult.Status getStatus() {
        for (SelftestResult result : results) {
            if (result.getStatus() == FEILET) {
                return FEILET;
            }
        }

        return OK;
    }

    public void addResults(List<SelftestResult> results) {
        this.results.addAll(results);
    }
}

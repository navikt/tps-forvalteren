package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models;

import java.util.ArrayList;
import java.util.List;

import static no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.SelftestResult.Status.FEILET;
import static no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.SelftestResult.Status.OK;


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

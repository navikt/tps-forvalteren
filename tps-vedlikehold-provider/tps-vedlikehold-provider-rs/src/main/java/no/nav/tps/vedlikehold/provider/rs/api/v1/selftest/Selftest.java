package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest;

import no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.SelftestResult;

@FunctionalInterface
public interface Selftest {

    SelftestResult perform();
}

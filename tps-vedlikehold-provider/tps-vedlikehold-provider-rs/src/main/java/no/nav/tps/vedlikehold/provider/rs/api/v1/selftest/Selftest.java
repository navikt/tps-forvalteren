package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest;

import no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.SelftestResult;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public interface Selftest {

    SelftestResult perform();
}

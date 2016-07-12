package no.nav.tps.vedlikehold.provider.web.selftest;

import no.nav.tps.vedlikehold.provider.web.model.SelftestResult;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public interface Selftest {

    SelftestResult perform();
}

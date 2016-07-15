package no.nav.tps.vedlikehold.provider.web.selftest;

import no.nav.tps.vedlikehold.service.command.fasit.PingFasit;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Kristian Kyvik (Visma Consulting).
 */
public class FasitSelftest extends SubSystemSelftest {
    @Autowired
    private PingFasit pingFasit;

    @Override
    protected String getSubSystemName() {
        return "Fasit";
    }

    @Override
    protected boolean performCheck() throws Exception {
        pingFasit.execute();
        return true;
    }
}

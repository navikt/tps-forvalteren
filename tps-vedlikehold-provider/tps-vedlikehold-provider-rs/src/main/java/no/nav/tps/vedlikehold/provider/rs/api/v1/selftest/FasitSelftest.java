package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest;

import no.nav.tps.vedlikehold.service.command.fasit.PingFasit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kristian Kyvik (Visma Consulting).
 */
@Component
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

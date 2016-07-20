package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest;

import no.nav.tps.vedlikehold.service.command.tpsws.PingEgenAnsatt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Component
public class EgenAnsattSelftest extends SubSystemSelftest {

    @Autowired
    private PingEgenAnsatt pingEgenAnsatt;

    @Override
    protected String getSubSystemName() {
        return "TPSWS - EgenAnsatt";
    }

    @Override
    protected boolean performCheck() throws Exception {
        pingEgenAnsatt.execute();
        return true;
    }
}

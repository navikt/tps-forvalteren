package no.nav.tps.vedlikehold.provider.web.selftest;

import no.nav.tps.vedlikehold.service.command.vera.PingVera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Component
public class VeraSelftest extends SubSystemSelftest {

    @Autowired
    private PingVera pingVera;

    @Override
    protected String getSubSystemName() {
        return "Vera";
    }

    @Override
    protected boolean performCheck() throws Exception {
        pingVera.execute();
        return true;
    }
}
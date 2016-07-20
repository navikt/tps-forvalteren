package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest;

import no.nav.tps.vedlikehold.service.command.tps.PingTps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Component
public class TpsSelftest extends SubSystemSelftest {
    @Autowired
    private PingTps pingTps;

    @Override
    protected String getSubSystemName() {
        return "Tps";
    }

    @Override
    protected boolean performCheck() throws Exception {
        pingTps.execute();
        return true;
    }
}

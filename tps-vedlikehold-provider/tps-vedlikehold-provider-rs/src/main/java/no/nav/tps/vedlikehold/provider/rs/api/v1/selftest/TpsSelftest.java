package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest;

import no.nav.tps.vedlikehold.service.command.tps.PingTps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TpsSelftest extends SubSystemSelftest {
    @Autowired
    private PingTps pingTps;

    @Override
    protected String getSubSystemName() {
        return "TPS";
    }

    @Override
    protected boolean performCheck() throws Exception {
        pingTps.execute();
        return true;
    }
}

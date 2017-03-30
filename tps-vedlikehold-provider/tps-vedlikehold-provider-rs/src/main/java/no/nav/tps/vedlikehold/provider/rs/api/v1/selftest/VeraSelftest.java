package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest;

import no.nav.tps.vedlikehold.service.command.vera.PingVera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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

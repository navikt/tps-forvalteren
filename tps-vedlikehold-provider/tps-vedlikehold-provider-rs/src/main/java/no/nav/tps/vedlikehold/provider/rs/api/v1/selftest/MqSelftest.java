package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest;

import no.nav.tps.vedlikehold.service.command.mq.PingMq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kristian Kyvik (Visma Consulting).
 */
@Component
public class MqSelftest extends SubSystemSelftest {
    @Autowired
    private PingMq pingMq;

    @Override
    protected String getSubSystemName() {
        return "Mq";
    }

    @Override
    protected boolean performCheck() throws Exception {
        pingMq.execute();
        return true;
    }
}

package no.nav.tps.forvalteren.consumer.rs.environments;

import no.nav.freg.common.autoconfigure.selftest.spi.SelftestCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static no.nav.tps.forvalteren.consumer.rs.environments.FetchEnvironmentsManager.BASE_URL;

@Component
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false", matchIfMissing = true)
public class FetchEnvironmentsSelftestCheck implements SelftestCheck {

    @Autowired
    private FetchEnvironmentsConsumer consumer;

    private static final String FASIT_ALIAS = "deployLog_v1";

    @Override
    public boolean perform() {
        consumer.ping();
        return true;
    }

    @Override
    public String getDescription() {
        return "Ping av Tpsws";
    }

    @Override
    public String getEndpoint() {
        return "rs:action ping @ " + FASIT_ALIAS + " via " + BASE_URL;
    }

    @Override
    public boolean isVital() {
        return false;
    }
}

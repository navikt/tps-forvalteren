package no.nav.tps.forvalteren.consumer.rs.fasit;

import no.nav.freg.common.autoconfigure.selftest.spi.SelftestCheck;
import no.nav.tps.forvalteren.consumer.rs.fasit.config.FasitConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false", matchIfMissing = true)
public class FasitSelftest implements SelftestCheck {

    private static final String RS_ACTION_NAME = "ping";

    @Autowired
    private FasitClient consumer;

    @Override public boolean perform() {
        consumer.ping();
        return true;
    }

    @Override public String getDescription() {
        return "Ping av Fasit";
    }

    @Override public String getEndpoint() {
        return "rs:action @ " + RS_ACTION_NAME + " via " + FasitConstants.BASE_URL;
    }

    @Override public boolean isVital() {
        return true;
    }

}

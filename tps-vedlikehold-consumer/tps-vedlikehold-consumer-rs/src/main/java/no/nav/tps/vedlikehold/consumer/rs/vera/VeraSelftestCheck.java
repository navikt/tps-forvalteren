package no.nav.tps.vedlikehold.consumer.rs.vera;

import no.nav.freg.common.autoconfigure.selftest.spi.SelftestCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static no.nav.tps.vedlikehold.consumer.rs.vera.DefaultVeraConsumer.BASE_URL;

@Component
public class VeraSelftestCheck implements SelftestCheck {

    @Autowired
    private VeraConsumer consumer;

    private static final String FASIT_ALIAS = "deployLog_v1";

    @Override
    public boolean perform() {
        consumer.ping();
        return true;
    }

    @Override
    public String getDescription() {
        return "Ping av Vera";
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

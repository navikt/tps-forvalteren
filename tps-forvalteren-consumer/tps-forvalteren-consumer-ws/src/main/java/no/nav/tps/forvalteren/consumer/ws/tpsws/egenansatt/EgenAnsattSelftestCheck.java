package no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt;

import no.nav.freg.common.autoconfigure.selftest.spi.SelftestCheck;
import no.nav.tps.forvalteren.consumer.ws.tpsws.config.TpswsConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EgenAnsattSelftestCheck implements SelftestCheck {

    private static final String WS_ACTION_NAME = "ping";
    private static final String FASIT_ENDPOINT_ALIAS = "virksomhet:PipEgenAnsatt_v1";

    @Autowired
    private DefaultEgenAnsattConsumer consumer;

    @Autowired
    private TpswsConsumerConfig config;

    @Override
    public boolean perform() {
        consumer.ping();
        return true;
    }

    @Override
    public String getDescription() {
        return "Ping av PipEgenAnsatt_v1";
    }

    @Override
    public String getEndpoint() {
        return "ws:action " + WS_ACTION_NAME + " @ " + FASIT_ENDPOINT_ALIAS + " via " + config.getEgenAnsattAddress();
    }

    @Override public boolean isVital() {
        return true;
    }
}

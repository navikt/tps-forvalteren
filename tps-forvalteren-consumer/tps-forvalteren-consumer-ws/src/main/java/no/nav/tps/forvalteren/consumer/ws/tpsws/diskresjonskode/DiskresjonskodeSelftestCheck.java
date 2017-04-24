package no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode;

import no.nav.freg.common.autoconfigure.selftest.spi.SelftestCheck;
import no.nav.tps.forvalteren.consumer.ws.tpsws.config.TpswsConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiskresjonskodeSelftestCheck implements SelftestCheck {

    private static final String WS_ACTION_NAME = "ping";
    private static final String FASIT_ENDPOINT_ALIAS = "virksomhet:Diskresjonskode_v1";

    @Autowired
    private DiskresjonskodeConsumer consumer;

    @Autowired
    private TpswsConsumerConfig config;

    @Override
    public boolean perform() {
        consumer.ping();
        return true;
    }

    @Override
    public String getDescription() {
        return "Ping av diskresjonskode-v1";
    }

    @Override
    public String getEndpoint() {
        return "ws:action " + WS_ACTION_NAME + " @ " + FASIT_ENDPOINT_ALIAS + " via " + config.getDiskresjonskodeAddress();
    }

    @Override
    public boolean isVital() {
        return true;
    }
}

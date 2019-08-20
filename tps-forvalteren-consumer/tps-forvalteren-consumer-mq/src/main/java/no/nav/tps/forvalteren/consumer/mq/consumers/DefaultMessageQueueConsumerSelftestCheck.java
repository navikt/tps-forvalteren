package no.nav.tps.forvalteren.consumer.mq.consumers;

import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;

import no.nav.freg.common.autoconfigure.selftest.spi.SelftestCheck;

// @Component
public class DefaultMessageQueueConsumerSelftestCheck implements SelftestCheck {

    @Autowired
    private MessageQueueConsumer consumer;

    @Override
    public boolean perform() throws JMSException {
        consumer.ping();
        return true;
    }

    @Override
    public String getDescription() {
        return "Ping av TPS/MQ";
    }

    @Override
    public String getEndpoint() {
        return "mq:action ping @ TPS/MQ";
    }

    @Override
    public boolean isVital() {
        return true;
    }
}

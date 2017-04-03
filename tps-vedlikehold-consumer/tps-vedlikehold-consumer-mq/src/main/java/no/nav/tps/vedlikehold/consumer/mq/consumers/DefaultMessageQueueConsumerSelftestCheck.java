package no.nav.tps.vedlikehold.consumer.mq.consumers;

import no.nav.freg.common.autoconfigure.selftest.spi.SelftestCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
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

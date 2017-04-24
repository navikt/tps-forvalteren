package no.nav.tps.forvalteren.service.command.mq;

import no.nav.tps.forvalteren.service.command.Command;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PingMq implements Command {
    @Autowired
    private MessageQueueConsumer consumer;

    public void execute() throws Exception {
        consumer.ping();
    }
}
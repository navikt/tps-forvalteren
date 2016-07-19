package no.nav.tps.vedlikehold.service.command.mq;

import no.nav.tps.vedlikehold.consumer.mq.services.DefaultMessageQueueService;
import no.nav.tps.vedlikehold.consumer.mq.services.MessageQueueService;
import no.nav.tps.vedlikehold.service.command.Command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Service
public class PingMq implements Command {
    @Autowired
    private MessageQueueService consumer;

    public void execute() throws Exception {
        consumer.ping();
    }
}
package no.nav.tps.vedlikehold.service.command.vera;

import no.nav.tps.vedlikehold.service.command.Command;
import no.nav.tps.vedlikehold.consumer.rs.vera.VeraConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Service
public class PingVera implements Command {
    @Autowired
    private VeraConsumer consumer;

    public void execute() throws Exception {
        consumer.ping();
    }
}

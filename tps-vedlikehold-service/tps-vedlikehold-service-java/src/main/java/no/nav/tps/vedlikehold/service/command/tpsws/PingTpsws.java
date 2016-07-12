package no.nav.tps.vedlikehold.service.command.tpsws;

import no.nav.tps.vedlikehold.service.command.Command;

import org.springframework.stereotype.service;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Service
public class PingTpsws implements Command {
    @Autowired
    private KodeverkConsumer consumer;

    public void execute() {
        consumer.ping();
    }
}

package no.nav.tps.vedlikehold.service.command.tpsws;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Service
public class PingEgenAnsatt implements Command {
    @Autowired
    private EgenAnsattConsumer consumer;

    public void execute() throws Exception {
        consumer.ping();
    }
}

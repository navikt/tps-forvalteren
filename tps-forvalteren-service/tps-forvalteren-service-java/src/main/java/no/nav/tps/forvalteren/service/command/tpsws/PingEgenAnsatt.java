package no.nav.tps.forvalteren.service.command.tpsws;

import no.nav.tps.forvalteren.service.command.Command;
import no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PingEgenAnsatt implements Command {
    @Autowired
    private EgenAnsattConsumer consumer;

    public void execute() throws Exception {
        consumer.ping();
    }
}

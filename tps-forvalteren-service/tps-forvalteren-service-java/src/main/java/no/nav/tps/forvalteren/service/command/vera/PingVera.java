package no.nav.tps.forvalteren.service.command.vera;

import no.nav.tps.forvalteren.consumer.rs.vera.VeraConsumer;
import no.nav.tps.forvalteren.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PingVera implements Command {
    @Autowired
    private VeraConsumer consumer;

    public void execute() throws Exception {
        consumer.ping();
    }
}

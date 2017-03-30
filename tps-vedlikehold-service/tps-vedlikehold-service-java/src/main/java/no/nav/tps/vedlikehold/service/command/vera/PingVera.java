package no.nav.tps.vedlikehold.service.command.vera;

import no.nav.tps.vedlikehold.consumer.rs.vera.VeraConsumer;
import no.nav.tps.vedlikehold.service.command.Command;
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

package no.nav.tps.vedlikehold.service.command.fasit;

import no.nav.tps.vedlikehold.consumer.ws.fasit.FasitClient;
import no.nav.tps.vedlikehold.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PingFasit implements Command {
    @Autowired
    private FasitClient consumer;

    public void execute() throws Exception {
        consumer.ping();
    }
}

package no.nav.tps.forvalteren.service.command.fasit;

import no.nav.tps.forvalteren.consumer.rs.fasit.FasitClient;
import no.nav.tps.forvalteren.service.command.Command;
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

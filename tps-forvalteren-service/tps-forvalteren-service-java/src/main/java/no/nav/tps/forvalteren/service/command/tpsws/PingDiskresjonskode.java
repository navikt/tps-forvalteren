package no.nav.tps.forvalteren.service.command.tpsws;

import no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.forvalteren.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PingDiskresjonskode implements Command {
    @Autowired
    private DiskresjonskodeConsumer consumer;

    public void execute() throws Exception {
        consumer.ping();
    }
}

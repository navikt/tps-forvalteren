package no.nav.tps.forvalteren.service.command.vera;

import no.nav.tps.forvalteren.consumer.rs.vera.VeraConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class GetEnvironments {

    @Autowired
    private VeraConsumer veraConsumer;

    public Set<String> execute(String application) {
        return veraConsumer.getEnvironments(application);
    }
}

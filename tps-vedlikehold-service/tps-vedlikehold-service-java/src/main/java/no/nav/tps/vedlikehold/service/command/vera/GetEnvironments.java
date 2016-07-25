package no.nav.tps.vedlikehold.service.command.vera;

import java.util.Set;
import no.nav.tps.vedlikehold.consumer.rs.vera.VeraConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Service
public class GetEnvironments {

    @Autowired
    private VeraConsumer veraConsumer;

    public Set<String> execute(String application) {
        return veraConsumer.getEnvironments(application);
    }
}

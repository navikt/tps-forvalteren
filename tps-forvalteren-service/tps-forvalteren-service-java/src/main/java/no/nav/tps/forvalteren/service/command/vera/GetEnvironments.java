package no.nav.tps.forvalteren.service.command.vera;

import no.nav.tps.forvalteren.consumer.rs.environments.FetchEnvironmentsConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class GetEnvironments {

    @Autowired
    private FetchEnvironmentsConsumer fetchEnvironmentsConsumer;

    public Set<String> getEnvironmentsFromFasit(String application) {
        return fetchEnvironmentsConsumer.getEnvironments(application);
    }
}

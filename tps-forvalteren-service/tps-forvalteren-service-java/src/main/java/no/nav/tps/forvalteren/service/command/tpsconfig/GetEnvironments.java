package no.nav.tps.forvalteren.service.command.tpsconfig;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;


@Service
public class GetEnvironments {

    @Autowired
    private FasitApiConsumer fasitApiConsumer;

    public Set<String> getEnvironmentsFromFasit(String application) {
        return fasitApiConsumer.getEnvironments(application);
    }
}

package no.nav.tps.forvalteren.service.command.tpsconfig;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;

@Service
public class GetEnvironments {

    @Autowired
    private FasitApiConsumer fasitApiConsumer;

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    public Set<String> getEnvironmentsFromFasit(String application) {
        Set<String> environments = fasitApiConsumer.getEnvironments(application);

        return filterEnvironmentsOnDeployedEnvironment.execute(environments);
    }
}

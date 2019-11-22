package no.nav.tps.forvalteren.service.command.tpsconfig;

import static no.nav.tps.forvalteren.common.java.config.CacheConfig.CACHE_FASIT;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;

@Service
public class GetEnvironments {

    @Autowired
    private FasitApiConsumer fasitApiConsumer;

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @Cacheable(CACHE_FASIT)
    public Set<String> getEnvironmentsFromFasit(String application) {
        Set<String> environments = fasitApiConsumer.getEnvironments(application);
        environments.remove("t13");

        return filterEnvironmentsOnDeployedEnvironment.execute(environments);
    }
}

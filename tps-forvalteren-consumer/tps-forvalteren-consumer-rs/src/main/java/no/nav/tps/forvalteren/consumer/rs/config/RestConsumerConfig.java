package no.nav.tps.forvalteren.consumer.rs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.tps.forvalteren.consumer.rs.environments.config.FetchEnvironmentsConsumerConfig;
import no.nav.tps.forvalteren.consumer.rs.fasit.config.FasitConfig;
import no.nav.tps.forvalteren.consumer.rs.identpool.config.IdentpoolConfig;

@Configuration
@Import({
        FetchEnvironmentsConsumerConfig.class,
        FasitConfig.class,
        IdentpoolConfig.class
})
public class RestConsumerConfig {
}
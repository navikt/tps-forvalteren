package no.nav.tps.forvalteren.consumer.rs.config;

import no.nav.tps.forvalteren.consumer.rs.environments.config.FetchEnvironmentsConsumerConfig;
import no.nav.tps.forvalteren.consumer.rs.fasit.config.FasitConfig;
import no.nav.tps.forvalteren.consumer.rs.identpool.IdentPoolClient;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({
        FetchEnvironmentsConsumerConfig.class,
        FasitConfig.class,
        IdentPoolClient.class
})
public class RestConsumerConfig {
}
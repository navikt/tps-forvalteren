package no.nav.tps.forvalteren.consumer.rs.config;

import no.nav.tps.forvalteren.consumer.rs.environments.FetchEnvironmentsConsumer;
import no.nav.tps.forvalteren.consumer.rs.environments.config.FetchEnvironmentsConsumerConfig;
import no.nav.tps.forvalteren.consumer.rs.fasit.config.FasitConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({
        FetchEnvironmentsConsumerConfig.class,
        FasitConfig.class
})
public class RestConsumerConfig {
}
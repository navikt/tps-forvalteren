package no.nav.tps.forvalteren.consumer.rs.environments.config;

import no.nav.tps.forvalteren.consumer.rs.environments.FetchEnvironmentsConsumer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses = {
        FetchEnvironmentsConsumer.class
})
public class FetchEnvironmentsConsumerConfig {
}
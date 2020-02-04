package no.nav.tps.forvalteren.consumer.rs.environments.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import no.nav.tps.forvalteren.common.java.mapping.MapperConfig;
import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;

@Configuration
@ComponentScan(basePackageClasses = {
        FasitApiConsumer.class,
        MapperConfig.class
})
public class FetchEnvironmentsConsumerConfig {
}
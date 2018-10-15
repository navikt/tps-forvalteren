package no.nav.tps.forvalteren.consumer.rs.environments.config;

import no.nav.tps.forvalteren.common.java.mapping.MapperConfig;
import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses = {
        FasitApiConsumer.class,
        MapperConfig.class
})
public class FetchEnvironmentsConsumerConfig {
}
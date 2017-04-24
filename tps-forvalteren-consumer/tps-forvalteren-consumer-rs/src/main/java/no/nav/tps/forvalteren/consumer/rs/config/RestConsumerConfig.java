package no.nav.tps.forvalteren.consumer.rs.config;

import no.nav.tps.forvalteren.consumer.rs.vera.config.VeraConsumerConfig;
import no.nav.tps.forvalteren.consumer.rs.fasit.config.FasitConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({
        VeraConsumerConfig.class,
        FasitConfig.class
})
public class RestConsumerConfig {
}
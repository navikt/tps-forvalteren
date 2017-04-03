package no.nav.tps.vedlikehold.consumer.rs.config;

import no.nav.tps.vedlikehold.consumer.rs.fasit.config.FasitConfig;
import no.nav.tps.vedlikehold.consumer.rs.vera.config.VeraConsumerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({
        VeraConsumerConfig.class,
        FasitConfig.class
})
public class RestConsumerConfig {
}
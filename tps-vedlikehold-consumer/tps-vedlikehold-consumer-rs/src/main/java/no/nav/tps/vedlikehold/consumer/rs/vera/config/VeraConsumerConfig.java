package no.nav.tps.vedlikehold.consumer.rs.vera.config;

import no.nav.tps.vedlikehold.consumer.rs.vera.VeraConsumer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses = {
        VeraConsumer.class
})
public class VeraConsumerConfig {
}
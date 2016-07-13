package no.nav.tps.vedlikehold.consumer.rs.vera.config;

import no.nav.tps.vedlikehold.consumer.rs.vera.VeraConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Configuration
public class VeraConsumerConfig {

    @Bean
    public VeraConsumer veraConsumer() {
        return new VeraConsumer();
    }
}
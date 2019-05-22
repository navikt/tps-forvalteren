package no.nav.tps.forvalteren.consumer.rs.identpool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.tps.forvalteren.consumer.rs.identpool.IdentpoolConsumer;

@Configuration
public class IdentpoolConfig {

    @Bean
    public IdentpoolConsumer identpoolConsumer() {
        return new IdentpoolConsumer();
    }
}

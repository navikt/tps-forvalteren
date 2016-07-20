package no.nav.tps.vedlikehold.consumer.rs.vera.config;

import no.nav.tps.vedlikehold.consumer.rs.vera.VeraConsumer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Configuration
@ComponentScan(basePackageClasses = {
        VeraConsumer.class
})
public class VeraConsumerConfig {
}
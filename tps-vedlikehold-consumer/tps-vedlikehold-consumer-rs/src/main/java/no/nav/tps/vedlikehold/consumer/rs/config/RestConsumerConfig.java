package no.nav.tps.vedlikehold.consumer.rs.config;

import no.nav.tps.vedlikehold.consumer.rs.vera.config.VeraConsumerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Configuration
@Import({
        VeraConsumerConfig.class
})
public class RestConsumerConfig {

}
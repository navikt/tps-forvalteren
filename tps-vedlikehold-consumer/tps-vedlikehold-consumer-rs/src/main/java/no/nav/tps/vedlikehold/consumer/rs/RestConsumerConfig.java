package no.nav.tps.vedlikehold.consumer.rs;

import no.nav.tps.vedlikehold.consumer.rs.vera.config.VeraConsumerConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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


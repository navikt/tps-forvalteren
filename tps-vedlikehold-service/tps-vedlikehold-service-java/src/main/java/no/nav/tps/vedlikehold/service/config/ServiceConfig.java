package no.nav.tps.vedlikehold.service.config;

import no.nav.tps.vedlikehold.consumer.rs.config.RestConsumerConfig;
import no.nav.tps.vedlikehold.consumer.ws.WebServiceConsumerConfig;
import no.nav.tps.vedlikehold.service.command.config.CommandConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Configuration
@Import({
        CommandConfig.class,
        RestConsumerConfig.class,
        WebServiceConsumerConfig.class
})
public class ServiceConfig {
}
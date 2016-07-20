package no.nav.tps.vedlikehold.service.config;

import no.nav.tps.vedlikehold.consumer.mq.config.MessageQueueConsumerConfig;
import no.nav.tps.vedlikehold.consumer.rs.config.RestConsumerConfig;
import no.nav.tps.vedlikehold.consumer.ws.config.WebServiceConsumerConfig;
import no.nav.tps.vedlikehold.service.command.config.CommandConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Configuration
@EnableScheduling
@Import({
        CommandConfig.class,
        RestConsumerConfig.class,
        WebServiceConsumerConfig.class,
        MessageQueueConsumerConfig.class
})
public class ServiceConfig {
}
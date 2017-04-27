package no.nav.tps.forvalteren.service.config;

import no.nav.tps.forvalteren.service.command.config.CommandConfig;
import no.nav.tps.forvalteren.consumer.mq.config.MessageQueueConsumerConfig;
import no.nav.tps.forvalteren.consumer.rs.config.RestConsumerConfig;
import no.nav.tps.forvalteren.consumer.ws.config.WebServiceConsumerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;


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
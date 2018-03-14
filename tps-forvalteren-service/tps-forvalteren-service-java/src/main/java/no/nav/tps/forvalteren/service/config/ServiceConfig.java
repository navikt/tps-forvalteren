package no.nav.tps.forvalteren.service.config;

import no.nav.tps.forvalteren.consumer.mq.config.MessageQueueConsumerConfig;
import no.nav.tps.forvalteren.consumer.rs.config.RestConsumerConfig;
import no.nav.tps.forvalteren.consumer.ws.config.WebServiceConsumerConfig;
import no.nav.tps.forvalteren.consumer.ws.kodeverk.config.KodeverkConsumerConfig;
import no.nav.tps.forvalteren.domain.service.config.DomainServiceConfig;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableAutoConfiguration
@EnableScheduling
@Import({
        KodeverkConsumerConfig.class,
        RestConsumerConfig.class,
        WebServiceConsumerConfig.class,
        MessageQueueConsumerConfig.class,
        DomainServiceConfig.class
})
@ComponentScan(basePackages = "no.nav.tps.forvalteren.service")
public class ServiceConfig {
}
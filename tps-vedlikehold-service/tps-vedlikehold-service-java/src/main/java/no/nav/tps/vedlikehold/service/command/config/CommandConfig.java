package no.nav.tps.vedlikehold.service.command.config;

import no.nav.tps.vedlikehold.consumer.mq.factories.DefaultMessageQueueServiceFactory;
import no.nav.tps.vedlikehold.consumer.mq.services.DefaultMessageQueueService;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DefaultDiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.DefaultEgenAnsattConsumer;
import no.nav.tps.vedlikehold.service.command.Command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
\ */
@Configuration
@ComponentScan(basePackageClasses = Command.class)
public class CommandConfig {

    @Autowired
    DefaultMessageQueueServiceFactory defaultMessageQueueServiceFactory;

    @Bean
    DefaultDiskresjonskodeConsumer defaultDiskresjonskodeConsumer() {
        return new DefaultDiskresjonskodeConsumer();
    }

    @Bean
    DefaultEgenAnsattConsumer defaultEgenAnsattConsumer() {
        return new DefaultEgenAnsattConsumer();
    }

    @Bean
    DefaultMessageQueueService defaultMessageQueueService() throws Exception {
        DefaultMessageQueueService defaultMessageQueueService = defaultMessageQueueServiceFactory.createMessageQueueService("t4");
        return defaultMessageQueueService;
    }
}

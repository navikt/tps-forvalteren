package no.nav.tps.vedlikehold.service.command.config;

import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.consumer.mq.services.MessageQueueConsumer;
import no.nav.tps.vedlikehold.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
\ */
@Configuration
@ComponentScan(basePackageClasses = Command.class)
public class CommandConfig {

    @Autowired
    MessageQueueServiceFactory messageQueueServiceFactory;

    @Bean
    MessageQueueConsumer defaultMessageQueueService() throws Exception {
        MessageQueueConsumer defaultMessageQueueConsumer = messageQueueServiceFactory.createMessageQueueService("t4");
        return defaultMessageQueueConsumer;
    }
}

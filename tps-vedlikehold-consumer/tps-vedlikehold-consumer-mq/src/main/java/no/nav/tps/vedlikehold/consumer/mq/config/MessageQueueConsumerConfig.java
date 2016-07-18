package no.nav.tps.vedlikehold.consumer.mq.config;


import no.nav.tps.vedlikehold.consumer.mq.MessageQueueConsumerPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Configuration
@ComponentScan(basePackageClasses = {
        MessageQueueConsumerPackage.class
})
public class MessageQueueConsumerConfig {}

package no.nav.tps.vedlikehold.consumer.mq.config;


import no.nav.tps.vedlikehold.consumer.mq.PackageMarker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Configuration
@ComponentScan(basePackageClasses = {
        PackageMarker.class
})
public class MessageQueueConsumerConfig {}

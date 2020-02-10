package no.nav.tps.forvalteren.consumer.mq.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import no.nav.tps.forvalteren.consumer.mq.PackageMarker;

@Configuration
@ComponentScan(basePackageClasses = {
        PackageMarker.class
})
public class MessageQueueConsumerConfig {
}

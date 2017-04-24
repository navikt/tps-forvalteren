package no.nav.tps.forvalteren.consumer.mq.config;

import no.nav.tps.forvalteren.consumer.mq.PackageMarker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



@Configuration
@ComponentScan(basePackageClasses = {
        PackageMarker.class
})
public class MessageQueueConsumerConfig {
}

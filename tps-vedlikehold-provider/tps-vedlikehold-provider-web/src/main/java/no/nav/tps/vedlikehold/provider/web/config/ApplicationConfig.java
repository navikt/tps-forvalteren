package no.nav.tps.vedlikehold.provider.web.config;

import no.nav.tps.vedlikehold.consumer.mq.config.MessageQueueConsumerConfig;
import no.nav.tps.vedlikehold.consumer.ws.WebServiceConsumerConfig;
import no.nav.tps.vedlikehold.provider.rs.RestProviderConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kristian Kyvik (Visma Conuslting AS).
 */
@Configuration
@Import({
        WebServiceConsumerConfig.class,
        MessageQueueConsumerConfig.class,
        WebProviderConfig.class,
        RestProviderConfig.class
})
public class ApplicationConfig {
}

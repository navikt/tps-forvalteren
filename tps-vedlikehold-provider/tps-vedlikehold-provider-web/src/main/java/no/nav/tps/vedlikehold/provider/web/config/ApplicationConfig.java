package no.nav.tps.vedlikehold.provider.web.config;

import no.nav.tps.vedlikehold.consumer.mq.MessageQueueConfig;
import no.nav.tps.vedlikehold.consumer.ws.WebServiceConfig;
import no.nav.tps.vedlikehold.provider.rs.config.RestConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kristian Kyvik (Visma Conuslting AS).
 */
@Configuration
@Import({
        WebConfig.class,
        RestConfig.class,
        WebServiceConfig.class,
        MessageQueueConfig.class
})
public class ApplicationConfig {
}

package no.nav.tps.vedlikehold.provider.web.config;

import no.nav.tps.vedlikehold.consumer.rs.RestConsumerConfig;
import no.nav.tps.vedlikehold.consumer.ws.WebServiceConsumerConfig;
import no.nav.tps.vedlikehold.provider.rs.RestProviderConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kristian Kyvik (Visma Conuslting AS).
 */
@Configuration
@Import({
        RestConsumerConfig.class,
        WebServiceConsumerConfig.class,
        WebProviderConfig.class,
        RestProviderConfig.class
})
public class ApplicationConfig {
}

package no.nav.tps.vedlikehold.provider.web.config;

import no.nav.tps.vedlikehold.common.java.config.CommonConfig;
import no.nav.tps.vedlikehold.consumer.rs.RestConsumerConfig;
import no.nav.tps.vedlikehold.consumer.mq.config.MessageQueueConsumerConfig;
import no.nav.tps.vedlikehold.consumer.ws.config.WebServiceConsumerConfig;
import no.nav.tps.vedlikehold.provider.rs.config.RestProviderConfig;
import no.nav.tps.vedlikehold.service.config.ServiceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kristian Kyvik (Visma Conuslting AS).
 */
@Configuration
@Import({
        RestConsumerConfig.class,
        WebServiceConsumerConfig.class,
        MessageQueueConsumerConfig.class,
        WebProviderConfig.class,
        RestProviderConfig.class,
        ServiceConfig.class,
        CommonConfig.class
})

public class ApplicationConfig {

}

package no.nav.tps.vedlikehold.consumer.ws;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.config.TpswsConsumerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Configuration
@Import({
        TpswsConsumerConfig.class
})
public class WebServiceConfig {
}
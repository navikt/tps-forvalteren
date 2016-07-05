package no.nav.tps.vedlikehold.consumer.ws;

import no.nav.tps.vedlikehold.consumer.ws.fasit.FasitConfig;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.config.TpswsConsumerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Øyvind Grimnes, Visma Consulting AS on 29.06.2016.
 */

@Configuration
@Import({
        FasitConfig.class,
        TpswsConsumerConfig.class
})
public class WebServiceConfig {
}
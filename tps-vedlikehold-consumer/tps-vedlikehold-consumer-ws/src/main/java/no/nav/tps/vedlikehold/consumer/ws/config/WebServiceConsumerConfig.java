package no.nav.tps.vedlikehold.consumer.ws.config;

import no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConfig;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.config.TpswsConsumerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Configuration
@Import({
        FasitConfig.class,
        TpswsConsumerConfig.class
})
public class WebServiceConsumerConfig {}

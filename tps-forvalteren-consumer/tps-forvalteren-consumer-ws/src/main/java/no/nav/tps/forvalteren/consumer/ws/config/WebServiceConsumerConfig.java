package no.nav.tps.forvalteren.consumer.ws.config;

import no.nav.tps.forvalteren.consumer.ws.kodeverk.config.KodeverkConsumerConfig;
import no.nav.tps.forvalteren.consumer.ws.tpsws.config.TpswsConsumerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({
        TpswsConsumerConfig.class,
        KodeverkConsumerConfig.class
})
public class WebServiceConsumerConfig {}

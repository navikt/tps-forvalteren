package no.nav.tps.forvalteren.consumer.ws.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.tps.forvalteren.consumer.ws.kodeverk.config.KodeverkConsumerConfig;
import no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConfig;
import no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt.EgenAnsattConfig;

@Configuration
@Import({
        KodeverkConsumerConfig.class,
        DiskresjonskodeConfig.class,
        EgenAnsattConfig.class
})
public class WebServiceConsumerConfig {}

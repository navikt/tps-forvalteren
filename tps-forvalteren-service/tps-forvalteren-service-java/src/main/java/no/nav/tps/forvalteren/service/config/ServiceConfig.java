package no.nav.tps.forvalteren.service.config;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import no.nav.tps.forvalteren.consumer.mq.config.MessageQueueConsumerConfig;
import no.nav.tps.forvalteren.consumer.rs.config.RestConsumerConfig;
import no.nav.tps.forvalteren.consumer.ws.config.WebServiceConsumerConfig;
import no.nav.tps.forvalteren.domain.service.config.DomainServiceConfig;
import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;

@Configuration
@EnableAutoConfiguration
@EnableScheduling
@Import({
        RestConsumerConfig.class,
        WebServiceConsumerConfig.class,
        MessageQueueConsumerConfig.class,
        DomainServiceConfig.class
})
@ComponentScan(basePackages = "no.nav.tps.forvalteren.service")
public class ServiceConfig {

    @Bean
    public JAXBContext tpsDataS302Context() throws JAXBException {

        return JAXBContext.newInstance(TpsPersonData.class);
    }
}
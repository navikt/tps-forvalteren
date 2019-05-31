package no.nav.tps.forvalteren.service.config;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import no.nav.tps.forvalteren.consumer.mq.config.MessageQueueConsumerConfig;
import no.nav.tps.forvalteren.consumer.rs.config.RestConsumerConfig;
import no.nav.tps.forvalteren.consumer.ws.config.WebServiceConsumerConfig;
import no.nav.tps.forvalteren.consumer.ws.kodeverk.config.KodeverkConsumerConfig;
import no.nav.tps.forvalteren.domain.service.config.DomainServiceConfig;
import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;

@Configuration
@EnableAutoConfiguration
@EnableScheduling
@Import({
        KodeverkConsumerConfig.class,
        RestConsumerConfig.class,
        WebServiceConsumerConfig.class,
        MessageQueueConsumerConfig.class,
        DomainServiceConfig.class
})
@ComponentScan(basePackages = "no.nav.tps.forvalteren.service")
public class ServiceConfig {

    @Bean
    public Unmarshaller tpsDataS302Unmarshaller() throws JAXBException {

        JAXBContext jc = JAXBContext.newInstance(TpsPersonData.class);
        return jc.createUnmarshaller();
    }

    @Bean
    public Marshaller tpsDataS302Marshaller() throws JAXBException {

        JAXBContext jc = JAXBContext.newInstance(TpsPersonData.class);
        return jc.createMarshaller();
    }
}
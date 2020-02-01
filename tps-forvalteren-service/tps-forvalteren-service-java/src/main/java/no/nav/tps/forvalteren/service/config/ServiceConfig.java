package no.nav.tps.forvalteren.service.config;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;

@Configuration
//@EnableAutoConfiguration
@EnableScheduling
//@Import({
//        KodeverkConsumerConfig.class,
//        RestConsumerConfig.class,
//        WebServiceConsumerConfig.class,
//        MessageQueueConsumerConfig.class,
//        DomainServiceConfig.class
//})
//@ComponentScan(basePackages = "no.nav.tps.forvalteren.service")
public class ServiceConfig {

    @Bean
    public JAXBContext tpsDataS302Context() throws JAXBException {

        return JAXBContext.newInstance(TpsPersonData.class);
    }
}
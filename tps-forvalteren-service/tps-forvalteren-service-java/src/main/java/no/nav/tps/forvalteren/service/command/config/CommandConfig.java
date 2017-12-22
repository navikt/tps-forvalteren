package no.nav.tps.forvalteren.service.command.config;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.*;
import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.xml.XmlMapper;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAarsakskode43;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.VigselAarsakskode11;
import no.nav.tps.forvalteren.service.command.Command;

@Configuration
@ComponentScan(basePackageClasses = Command.class)
public class CommandConfig {

    private static final String DEFAULT_ENVIRONMENT_NUMBER = "6";

    @Autowired
    MessageQueueServiceFactory messageQueueServiceFactory;

    @Value("${environment.class}")
    private String deployedEnvironment;

    @Bean
    MessageQueueConsumer defaultMessageQueueService() throws Exception {
        String defaultEnvironment = deployedEnvironment + DEFAULT_ENVIRONMENT_NUMBER;
        return messageQueueServiceFactory.createMessageQueueConsumer(defaultEnvironment, REQUEST_QUEUE_SERVICE_RUTINE_ALIAS);
    }

    @Bean
    XmlMapper xmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        xmlMapper.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return xmlMapper;
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }

    @Bean
    ServiceRoutineResolver hentGT() {
        return new S610HentGT();
    }

    @Bean
    ServiceRoutineResolver hentTknr() { return new S002HentTknr();}

    @Bean
    ServiceRoutineResolver hentAdresser() {return new S103HentAdresser();}

    @Bean
    ServiceRoutineResolver hentAdresseLinjehistorikk() {
        return new S015HentAdresselinjehistorikk();
    }

    @Bean
    ServiceRoutineResolver hentAdressehistorikk() {
        return new S010Adressehistorikk();
    }

    @Bean
    ServiceRoutineResolver hentUtvandring() {
        return new S016Utvandring();
    }

    @Bean
    ServiceRoutineResolver hentHistorieForFlereFnr() {
        return new M201HentFnrNavnDiskresjonPaFlerePersoner();
    }

    @Bean
    ServiceRoutineResolver hentKontaktinformasjon(){ return new S600HentKontaktinformasjon();}

    @Bean
    ServiceRoutineResolver hentPersonSok() {
        return new S050SokUtFraNavnBostedAlderFnrServiceRoutineResolver();
    }

    @Bean
    ServiceRoutineResolver hentFnrHistorikk() {return new S011HentFNRDNRHistorikk();}

    @Bean
    ServiceRoutineResolver hentRelasjoner() {return new S005Relasjoner();}

    @Bean
    ServiceRoutineResolver hentPersonopplysninger() {return new S004HentPersonopplysninger();}

    @Bean
    SkdMeldingResolver innvandring() {
        return new InnvandringAarsakskode02();
    }

    @Bean
    SkdMeldingResolver vigsel() {
        return new VigselAarsakskode11();
    }

    @Bean
    SkdMeldingResolver doedsmelding() {
        return new DoedsmeldingAarsakskode43();
    }

}

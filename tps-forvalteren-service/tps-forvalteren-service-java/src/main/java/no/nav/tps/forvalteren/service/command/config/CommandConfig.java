package no.nav.tps.forvalteren.service.command.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.M201HentFnrNavnDiskresjonPaFlerePersoner;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S010Adressehistorikk;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S015HentAdresselinjehistorikk;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S016Utvandring;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S050SokUtFraNavnBostedAlderFnrServiceRoutineResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S610HentGT;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.ServiceRoutineResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.VigselAarsakskode11;
import no.nav.tps.forvalteren.service.command.Command;
import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;

@Configuration
@ComponentScan(basePackageClasses = Command.class)
public class CommandConfig {

    private static final String DEFAULT_ENVIRONMENT_NUMBER = "6";

    @Value("${environment.class}")
    private String deployedEnvironment;

    @Autowired
    MessageQueueServiceFactory messageQueueServiceFactory;

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
    ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }

    @Bean
    ServiceRoutineResolver hentGT(){
        return new S610HentGT();
    }

    @Bean
    ServiceRoutineResolver hentAdresseLinjehistorikk(){
        return new S015HentAdresselinjehistorikk();
    }

    @Bean
    ServiceRoutineResolver hentAdressehistorikk(){
        return new S010Adressehistorikk();
    }

    @Bean
    ServiceRoutineResolver hentUtvandring(){
        return new S016Utvandring();
    }

    @Bean
    ServiceRoutineResolver hentHistorieForFlereFnr(){
        return new M201HentFnrNavnDiskresjonPaFlerePersoner();
    }

    @Bean
    ServiceRoutineResolver hentPersonSok() {
        return new S050SokUtFraNavnBostedAlderFnrServiceRoutineResolver();
    }

    @Bean
    SkdMeldingResolver innvandring() {
        return new InnvandringAarsakskode02();
    }

    @Bean
    SkdMeldingResolver vigsel() {
        return new VigselAarsakskode11();
    }

}

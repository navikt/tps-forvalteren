package no.nav.tps.forvalteren.service.command.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.M201HentFnrNavnDiskresjonPaFlerePersoner;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.S010Adressehistorikk;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.S015HentAdresselinjehistorikk;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.S016Utvandring;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.S610HentGT;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.ServiceRoutineResolver;
import no.nav.tps.forvalteren.service.command.Command;
import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;

@Configuration
@ComponentScan(basePackageClasses = Command.class)
public class CommandConfig {

    private static final String DEFAULT_ENV = "t4";

    @Autowired
    MessageQueueServiceFactory messageQueueServiceFactory;

    @Bean
    MessageQueueConsumer defaultMessageQueueService() throws Exception {
        return messageQueueServiceFactory.createMessageQueueConsumer(DEFAULT_ENV, REQUEST_QUEUE_SERVICE_RUTINE_ALIAS);
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
}

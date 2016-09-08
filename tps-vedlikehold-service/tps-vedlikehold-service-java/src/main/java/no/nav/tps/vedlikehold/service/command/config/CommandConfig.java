package no.nav.tps.vedlikehold.service.command.config;

import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers.S000TilgangTilTpsServiceRoutineResolver;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers.S400HentPersonServiceRoutineResolver;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers.ServiceRoutineResolver;
import no.nav.tps.vedlikehold.service.command.Command;

import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.fasterxml.jackson.xml.XmlMapper;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
\ */
@Configuration
@ComponentScan(basePackageClasses = Command.class)
public class CommandConfig {

    @Autowired
    MessageQueueServiceFactory messageQueueServiceFactory;

    @Bean
    MessageQueueConsumer defaultMessageQueueService() throws Exception {
        return messageQueueServiceFactory.createMessageQueueService("t4");
    }

    @Bean
    XmlMapper xmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        xmlMapper.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return xmlMapper;
    }

    @Bean
    @Order(Integer.MAX_VALUE)
    ServiceRoutineResolver tilgangTilTpsServiceRoutineResolver() {
        return new S000TilgangTilTpsServiceRoutineResolver();
    }

    @Bean
    @Order(1)
    ServiceRoutineResolver hentPersonServiceRoutineResolver() {
        return new S400HentPersonServiceRoutineResolver();
    }
}

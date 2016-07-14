package no.nav.tps.vedlikehold.service.command.config;

import no.nav.tps.vedlikehold.consumer.rs.vera.VeraConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DefaultDiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.DefaultEgenAnsattConsumer;
import no.nav.tps.vedlikehold.service.command.Command;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.wsdl.wsdl11.provider.DefaultMessagesProvider;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Configuration
@ComponentScan(basePackageClasses = Command.class)
public class CommandConfig {
    @Bean
    DefaultDiskresjonskodeConsumer defaultDiskresjonskodeConsumer() {
        return new DefaultDiskresjonskodeConsumer();
    }

    @Bean
    DefaultEgenAnsattConsumer defaultEgenAnsattConsumer() {
        return new DefaultEgenAnsattConsumer();
    }
}


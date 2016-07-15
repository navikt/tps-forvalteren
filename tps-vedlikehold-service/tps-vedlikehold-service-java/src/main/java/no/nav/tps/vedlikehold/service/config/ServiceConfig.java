package no.nav.tps.vedlikehold.service.config;

import no.nav.tps.vedlikehold.service.command.Command;
import no.nav.tps.vedlikehold.service.command.config.CommandConfig;
import no.nav.tps.vedlikehold.service.command.servicerutiner.DefaultGetTpsServiceRutinerService;
import no.nav.tps.vedlikehold.service.command.servicerutiner.GetTpsServiceRutinerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Configuration
@Import(CommandConfig.class)
public class ServiceConfig {

}

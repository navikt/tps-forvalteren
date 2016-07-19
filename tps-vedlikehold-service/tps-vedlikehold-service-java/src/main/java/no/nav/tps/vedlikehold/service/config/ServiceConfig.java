package no.nav.tps.vedlikehold.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import no.nav.tps.vedlikehold.service.command.config.CommandConfig;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Configuration
@EnableScheduling
@Import(CommandConfig.class)
public class ServiceConfig {
}


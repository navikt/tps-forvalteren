package no.nav.tps.vedlikehold.service.config;

import no.nav.tps.vedlikehold.service.command.config.CommandConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Configuration
@Import(CommandConfig.class)
public class ServiceConfig {}

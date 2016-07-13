package no.nav.tps.vedlikehold.service.command.config;

import no.nav.tps.vedlikehold.service.command.Command;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Configuration
@ComponentScan(basePackageClasses = Command.class)
public class CommandConfig {
}


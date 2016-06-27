package no.nav.tps.vedlikehold.provider.rs.config;

import no.nav.tps.vedlikehold.provider.rs.security.config.RestSecurityConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */

@Configuration
@Import(RestSecurityConfig.class)
@EnableAutoConfiguration
public class WebConfig {
}

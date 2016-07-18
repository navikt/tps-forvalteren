package no.nav.tps.vedlikehold.provider.web.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *  @author Kristian Kyvik (Visma Consulting).
 */
@Configuration
@EnableAutoConfiguration
@Import(ApplicationConfig.class)
public class ApplicationServletInitializer extends SpringBootServletInitializer {
}

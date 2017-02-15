package no.nav.tps.vedlikehold.provider.web.config;

import no.nav.tps.vedlikehold.domain.repository.jpa.config.JpaRepositoryConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kristian Kyvik (Visma Conuslting AS).
 */
@Configuration
@Import({
        WebProviderConfig.class,
        JpaRepositoryConfig.class
})
public class ApplicationConfig {
}

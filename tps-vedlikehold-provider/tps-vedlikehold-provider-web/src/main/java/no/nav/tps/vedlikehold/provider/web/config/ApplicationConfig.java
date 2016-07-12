package no.nav.tps.vedlikehold.provider.web.config;

import no.nav.tps.vedlikehold.provider.rs.RestProviderConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kristian Kyvik (Visma Conuslting AS).
 */
@Configuration
@Import({
        WebProviderConfig.class,
        RestProviderConfig.class
})
public class ApplicationConfig {
}

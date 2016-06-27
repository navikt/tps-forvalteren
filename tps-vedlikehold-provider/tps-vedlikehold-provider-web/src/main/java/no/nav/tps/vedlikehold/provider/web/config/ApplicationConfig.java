package no.nav.tps.vedlikehold.provider.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kristian Kyvik (Visma Conuslting AS).
 */
@Configuration
@Import({
        WebConfig.class
})
public class ApplicationConfig {
}

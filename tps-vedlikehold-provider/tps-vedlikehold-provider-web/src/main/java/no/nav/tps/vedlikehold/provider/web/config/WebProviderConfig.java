package no.nav.tps.vedlikehold.provider.web.config;
import no.nav.tps.vedlikehold.provider.web.SelftestController;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Configuration
@EnableConfigurationProperties
@PropertySource("classpath:global.properties")
public class WebProviderConfig {
    @Bean
    SelftestController selftestController() {
        return new SelftestController();
    }
}

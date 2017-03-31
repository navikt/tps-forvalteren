package no.nav.tps.vedlikehold.provider.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        WebProviderConfig.class
})
public class ApplicationConfig {
}

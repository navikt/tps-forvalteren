package no.nav.tps.forvalteren.application.local;

import no.nav.tps.forvalteren.provider.web.config.ApplicationServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import(ApplicationServletInitializer.class)
public class LocalApplicationConfig {
}

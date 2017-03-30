package no.nav.tps.vedlikehold.application.local;

import no.nav.tps.vedlikehold.provider.web.config.ApplicationServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import(ApplicationServletInitializer.class)
public class LocalApplicationConfig {
}

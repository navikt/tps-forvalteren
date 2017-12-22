package no.nav.tps.forvalteren.provider.web.config;

import no.nav.tps.forvalteren.common.java.config.CommonConfig;
import no.nav.tps.forvalteren.common.java.mapping.MapperConfig;
import no.nav.tps.forvalteren.provider.rs.config.MetricsConfig;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryConfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        CommonConfig.class,
        WebProviderConfig.class,
        MetricsConfig.class,
        RepositoryConfig.class,
        MapperConfig.class
})
@ComponentScan
public class ApplicationConfig {
}

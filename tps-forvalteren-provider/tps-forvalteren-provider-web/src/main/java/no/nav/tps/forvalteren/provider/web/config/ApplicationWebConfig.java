package no.nav.tps.forvalteren.provider.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.tps.forvalteren.common.config.CommonConfig;
import no.nav.tps.forvalteren.common.mapping.MapperConfig;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryConfig;

@Configuration
@Import({
        CommonConfig.class,
        WebProviderConfig.class,
        RepositoryConfig.class,
        MapperConfig.class
})
@ComponentScan
public class ApplicationWebConfig {
}

package no.nav.tps.forvalteren;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.tps.forvalteren.common.config.CacheConfig;
import no.nav.tps.forvalteren.common.config.CommonConfig;
import no.nav.tps.forvalteren.common.mapping.MapperConfig;
import no.nav.tps.forvalteren.provider.rs.config.RestProviderConfig;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryConfig;

@Configuration
@EnableAutoConfiguration
@Import({
        CommonConfig.class,
        CacheConfig.class,
        RepositoryConfig.class,
        MapperConfig.class,
        RestProviderConfig.class
})
public class ApplicationConfig {

}

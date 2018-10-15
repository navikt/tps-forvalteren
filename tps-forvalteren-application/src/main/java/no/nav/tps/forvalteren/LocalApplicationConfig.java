package no.nav.tps.forvalteren;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.tps.forvalteren.common.java.config.CommonConfig;
import no.nav.tps.forvalteren.common.java.mapping.MapperConfig;
import no.nav.tps.forvalteren.provider.rs.config.RestProviderConfig;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryConfig;

@EnableCaching
@Configuration
@EnableAutoConfiguration
@Import({
        CommonConfig.class,
        RepositoryConfig.class,
        MapperConfig.class,
        RestProviderConfig.class
})
public class LocalApplicationConfig {

}

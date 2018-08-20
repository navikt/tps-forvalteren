package no.nav.tps.forvalteren;

import no.nav.tps.forvalteren.common.java.config.CommonConfig;
import no.nav.tps.forvalteren.common.java.mapping.MapperConfig;
import no.nav.tps.forvalteren.provider.rs.config.RestProviderConfig;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryConfig;
import no.nav.tps.forvalteren.skdavspilleren.repository.config.AvspillerRepositoryConfig;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({
        CommonConfig.class,
        RepositoryConfig.class,
        MapperConfig.class,
        RestProviderConfig.class,
        AvspillerRepositoryConfig.class
})
public class LocalApplicationConfig {

}

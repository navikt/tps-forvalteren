package no.nav.tps.forvalteren.provider.config;

import no.nav.tps.forvalteren.common.java.config.CommonConfig;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryConfig;
import no.nav.tps.forvalteren.service.config.ServiceConfig;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EntityScan("no.nav.tps.forvalteren.domain")
@EnableJpaRepositories("no.nav.tps.forvalteren.provider")
@Import({
        ServiceConfig.class,
        CommonConfig.class,
        RepositoryConfig.class
})
@ComponentScan(basePackages = "no.nav.tps.forvalteren.provider")
public class IntegrationTestConfig {

}

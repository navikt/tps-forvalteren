package config;

import no.nav.tps.vedlikehold.domain.repository.jpa.config.JpaRepositoryConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Peter Fløgstad on 14.02.2017.
 */

@Configuration
@Import(JpaRepositoryConfig.class)
@EnableAutoConfiguration
@PropertySource("classpath:flyway.properties")
public class RepositoryTestConfig {

}

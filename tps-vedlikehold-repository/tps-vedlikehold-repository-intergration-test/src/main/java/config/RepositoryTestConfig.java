package config;

import no.nav.tps.vedlikehold.domain.repository.jpa.config.JpaRepositoryConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repoes.TPSKTestPersonTestRepository;

/**
 * Created by Peter Fløgstad on 14.02.2017.
 */

@Configuration
@Import(JpaRepositoryConfig.class)
@EnableJpaRepositories(basePackageClasses = {
        TPSKTestPersonTestRepository.class
})
@EnableAutoConfiguration
public class RepositoryTestConfig {

}

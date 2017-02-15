package no.nav.tps.vedlikehold.domain.repository.jpa.config;

import no.nav.tps.vedlikehold.domain.repository.jpa.repoes.TPSKTestPersonRepository;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Peter Fløgstad on 26.01.2017.
 */

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "no.nav.tps.vedlikehold.domain.service.jpa")
@EnableJpaRepositories(basePackageClasses = {
        TPSKTestPersonRepository.class
})
@EnableJpaAuditing
public class JpaRepositoryConfig {

}


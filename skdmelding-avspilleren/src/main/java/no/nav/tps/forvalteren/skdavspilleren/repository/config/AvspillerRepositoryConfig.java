package no.nav.tps.forvalteren.skdavspilleren.repository.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.Avspillergruppe;
import no.nav.tps.forvalteren.skdavspilleren.repository.AvspillergruppeRepository;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackageClasses = Avspillergruppe.class)
@EnableJpaRepositories(basePackageClasses = AvspillergruppeRepository.class)
@EnableJpaAuditing
public class AvspillerRepositoryConfig {
}

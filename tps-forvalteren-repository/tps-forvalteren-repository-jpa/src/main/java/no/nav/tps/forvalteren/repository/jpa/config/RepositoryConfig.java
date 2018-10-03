package no.nav.tps.forvalteren.repository.jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Personmal;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonmalRepository;
import no.nav.tps.forvalteren.repository.jpa.UsernameAuditingAware;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackageClasses = {Person.class, Personmal.class})
@EnableJpaRepositories(basePackageClasses = {PersonRepository.class, PersonmalRepository.class})
@ComponentScan(basePackageClasses = {
        UsernameAuditingAware.class
})
@EnableJpaAuditing
public class RepositoryConfig {
}

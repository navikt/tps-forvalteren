package no.nav.tps.forvalteren.repository.jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackageClasses = {Person.class})
@EnableJpaRepositories(basePackageClasses = {PersonRepository.class})
//@ComponentScan(basePackageClasses = {
//        UsernameAuditingAware.class
//})
@EnableJpaAuditing
public class RepositoryConfig {
}

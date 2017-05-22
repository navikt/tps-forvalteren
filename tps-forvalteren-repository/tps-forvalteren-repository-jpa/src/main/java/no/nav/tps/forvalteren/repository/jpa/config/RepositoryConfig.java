package no.nav.tps.forvalteren.repository.jpa.config;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.UsernameAuditingAware;
import no.nav.tps.forvalteren.repository.jpa.converter.DateTimeToDateConverter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackageClasses = {Person.class, DateTimeToDateConverter.class})
@EnableJpaRepositories(basePackageClasses = PersonRepository.class)
@ComponentScan(basePackageClasses = {
        UsernameAuditingAware.class
})
@EnableJpaAuditing
public class RepositoryConfig {
}

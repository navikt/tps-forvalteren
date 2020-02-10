package no.nav.tps.forvalteren.repository.jpa.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.tps.forvalteren.repository.jpa.auditing.UsernameTestResolver;

@Configuration
@EnableAutoConfiguration
@Import(RepositoryConfig.class)
@ComponentScan(basePackageClasses = {
        UsernameTestResolver.class
})
public class RepositoryTestConfig {
}

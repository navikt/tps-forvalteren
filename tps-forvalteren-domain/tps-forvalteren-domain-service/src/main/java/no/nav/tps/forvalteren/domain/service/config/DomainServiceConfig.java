package no.nav.tps.forvalteren.domain.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import no.nav.tps.forvalteren.domain.service.RelasjonType;

@Configuration
@ComponentScan(basePackageClasses = RelasjonType.class)
public class DomainServiceConfig {

}

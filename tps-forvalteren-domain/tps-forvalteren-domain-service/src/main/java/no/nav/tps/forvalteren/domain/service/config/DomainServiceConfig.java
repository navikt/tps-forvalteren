package no.nav.tps.forvalteren.domain.service.config;

import no.nav.tps.forvalteren.domain.service.RelasjonType;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = RelasjonType.class)
public class DomainServiceConfig {

}

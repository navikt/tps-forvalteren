package no.nav.tps.vedlikehold.service.java;

import no.nav.tps.vedlikehold.service.java.authorisation.AuthorisationService;
import no.nav.tps.vedlikehold.service.java.service.rutine.ServiceRutineConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Configuration
@Import(
        ServiceRutineConfig.class
)
@ComponentScan(basePackageClasses = {
        AuthorisationService.class
})
public class ServiceConfig {
}

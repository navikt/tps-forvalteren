package no.nav.tps.vedlikehold.provider.web.config;

import no.nav.tps.vedlikehold.common.java.config.CommonConfig;
import no.nav.tps.vedlikehold.provider.rs.config.RestProviderConfig;
import no.nav.tps.vedlikehold.service.config.ServiceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kristian Kyvik (Visma Conuslting AS).
 */
@Configuration
@Import({
        WebProviderConfig.class,
        RestProviderConfig.class,
        ServiceConfig.class,
        CommonConfig.class
})
public class ApplicationConfig {

}

package no.nav.tps.vedlikehold.provider.rs.config;


import no.nav.tps.vedlikehold.provider.rs.api.v1.documentation.SwaggerConfig;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.UserController;
import no.nav.tps.vedlikehold.provider.rs.security.config.RestSecurityConfig;
import no.nav.tps.vedlikehold.provider.rs.security.config.WebSecurityConfig;
<<<<<<< HEAD:tps-vedlikehold-provider/tps-vedlikehold-provider-rs/src/main/java/no/nav/tps/vedlikehold/provider/rs/RestProviderConfig.java
=======
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
>>>>>>> b8f7c02c26f7c4624ef96b15c6905f81edb581e2:tps-vedlikehold-provider/tps-vedlikehold-provider-rs/src/main/java/no/nav/tps/vedlikehold/provider/rs/config/RestProviderConfig.java
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Configuration
@Import({
        SwaggerConfig.class,
        WebSecurityConfig.class,
        RestSecurityConfig.class,
})
@ComponentScan(basePackageClasses = {
        UserController.class
})
<<<<<<< HEAD:tps-vedlikehold-provider/tps-vedlikehold-provider-rs/src/main/java/no/nav/tps/vedlikehold/provider/rs/RestProviderConfig.java
public class RestProviderConfig {
}
=======
public class RestProviderConfig {}
>>>>>>> b8f7c02c26f7c4624ef96b15c6905f81edb581e2:tps-vedlikehold-provider/tps-vedlikehold-provider-rs/src/main/java/no/nav/tps/vedlikehold/provider/rs/config/RestProviderConfig.java

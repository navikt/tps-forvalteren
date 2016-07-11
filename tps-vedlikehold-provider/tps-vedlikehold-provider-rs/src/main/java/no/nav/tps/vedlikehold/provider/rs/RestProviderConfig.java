package no.nav.tps.vedlikehold.provider.rs;


import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.UserController;
import no.nav.tps.vedlikehold.provider.rs.api.v1.documentation.SwaggerConfig;
import no.nav.tps.vedlikehold.provider.rs.security.config.RestSecurityConfig;
import no.nav.tps.vedlikehold.provider.rs.security.config.WebSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@EnableAutoConfiguration
@Configuration
@Import({
        SwaggerConfig.class,
        WebSecurityConfig.class,
        RestSecurityConfig.class
})
@ComponentScan(basePackageClasses = {
        UserController.class
})
public class RestProviderConfig {

    public static void main(String[] args) {
        SpringApplication.run(RestProviderConfig.class, args);
    }
}

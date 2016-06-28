package no.nav.tps.vedlikehold.provider.rs.config;


import no.nav.tps.vedlikehold.provider.rs.security.config.RestSecurityConfig;
import no.nav.tps.vedlikehold.provider.rs.security.config.WebSecurityConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import no.nav.tps.vedlikehold.provider.rs.api.v1.UserController;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import org.springframework.boot.SpringApplication;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Configuration
@ComponentScan(basePackageClasses = {
        UserController.class,
        UserContextHolder.class,
        SwaggerConfig.class,
        WebSecurityConfig.class,
        RestSecurityConfig.class
})
public class ProviderRestConfig {

    public static void main(String[] args) {
        SpringApplication.run(ProviderRestConfig.class, args);
    }
}

package no.nav.tps.vedlikehold.provider.rs.config;


import no.nav.tps.vedlikehold.provider.rs.security.config.RestSecurityConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
        SwaggerConfig.class
})
@Import(RestSecurityConfig.class)
@EnableAutoConfiguration
public class WebConfig {

    public static void main(String[] args) {
        SpringApplication.run(WebConfig.class, args);
    }
}

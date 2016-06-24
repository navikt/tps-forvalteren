package no.nav.tps.vedlikehold.provider.rs.config;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

import no.nav.tps.vedlikehold.provider.rs.api.v1.UserController;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
        import org.springframework.context.annotation.ComponentScan;
        import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
        UserController.class,
        UserContextHolder.class
})
@EnableAutoConfiguration
public class WebConfig {

    public static void main(String[] args) {
        SpringApplication.run(WebConfig.class, args);
    }
}

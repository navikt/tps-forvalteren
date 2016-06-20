package no.nav.tps.vedlikehold.provider.web.config;

import no.nav.tps.vedlikehold.provider.web.api.v1.EnvironmentController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
@Configuration
@ComponentScan(basePackageClasses = {
        EnvironmentController.class
})
@EnableAutoConfiguration
public class WebConfig {

    public static void main(String[] args) {
        SpringApplication.run(WebConfig.class, args);
    }
}

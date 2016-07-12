package no.nav.tps.vedlikehold.service.config;

import no.nav.tps.vedlikehold.service.services.DefaultGetTpsServiceRutinerService;
import no.nav.tps.vedlikehold.service.services.GetTpsServiceRutinerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses = {
        GetTpsServiceRutinerService.class
})
public class ServiceConfig {

    @Bean
    GetTpsServiceRutinerService getTpsServiceRutinerService() {
        return new DefaultGetTpsServiceRutinerService();
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceConfig.class, args);
    }
}
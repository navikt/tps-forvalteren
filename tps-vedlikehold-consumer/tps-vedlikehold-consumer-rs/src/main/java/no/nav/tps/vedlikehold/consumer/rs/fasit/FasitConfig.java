package no.nav.tps.vedlikehold.consumer.rs.fasit;

import no.nav.aura.envconfig.client.ResourceTypeDO;
import no.nav.brevogarkiv.common.fasit.FasitClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.io.Console;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
        FasitPackageMarker.class
})
@PropertySource("classpath:fasit.properties")
public class FasitConfig {

    @Value("${fasit.base-url}")
    private String BASE_URL;

    @Bean
    public FasitClient getFasitClient(
            @Value("${fasit.username}") String username,
            @Value("${fasit.password}") String password) {
        return new FasitClient(BASE_URL, username, password);
    }

    public static void main(String[] args) {
        SpringApplication.run(FasitConfig.class, args);
    }

}

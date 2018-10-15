package no.nav.tps.forvalteren.consumer.rs.fasit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import no.nav.tps.forvalteren.consumer.rs.fasit.FasitClient;



@Configuration
@ComponentScan(basePackageClasses = {
        FasitClient.class
})
public class FasitConfig {

    @Bean
    public FasitClient getFasitClient() {
        return new FasitClient(
                FasitConstants.BASE_URL,
                FasitConstants.USERNAME,
                FasitConstants.PASSWORD
        );
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

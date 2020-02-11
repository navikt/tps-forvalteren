package no.nav.tps.forvalteren.consumer.rs.fasit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import no.nav.tps.forvalteren.consumer.rs.fasit.FasitClient;

@Configuration
@ComponentScan(basePackageClasses = {
        FasitClient.class
})
public class FasitConfig {

    @Value("${fasit.url}")
    private String fasitUrl;

    @Value("$fasit.username}")
    private String fasitUsername;

    @Value("${fasit.password}")
    private String password;

    @Bean
    public FasitClient getFasitClient() {
        return new FasitClient(
                fasitUrl,
                fasitUsername,
                password);
    }
}

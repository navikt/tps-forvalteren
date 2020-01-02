package no.nav.tps.forvalteren.consumer.rs.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import no.nav.tps.forvalteren.consumer.rs.environments.config.FetchEnvironmentsConsumerConfig;
import no.nav.tps.forvalteren.consumer.rs.fasit.config.FasitConfig;
import no.nav.tps.forvalteren.consumer.rs.identpool.config.IdentpoolConfig;

@Configuration
@Import({
        FetchEnvironmentsConsumerConfig.class,
        FasitConfig.class,
        IdentpoolConfig.class
})
public class RestConsumerConfig {

    private static final int TIMEOUT = 30_000;

    @Bean
    public RestTemplate restTemplate() {

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        return restTemplateBuilder
                .setConnectTimeout(TIMEOUT)
                .setReadTimeout(TIMEOUT)
                .build();
    }
}
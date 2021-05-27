package no.nav.tps.forvalteren.consumer.rs.config;

import java.net.ProxySelector;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import no.nav.tps.forvalteren.consumer.rs.adresser.config.AdresseConfig;
import no.nav.tps.forvalteren.consumer.rs.environments.config.FetchEnvironmentsConsumerConfig;
import no.nav.tps.forvalteren.consumer.rs.identpool.config.IdentpoolConfig;

@Configuration
@Import({
        FetchEnvironmentsConsumerConfig.class,
        IdentpoolConfig.class,
        AdresseConfig.class
})
public class RestConsumerConfig {

    private static final int TIMEOUT = 45_000;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

        return restTemplateBuilder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create()
                        .setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()))
                        .setSSLHostnameVerifier(new DefaultHostnameVerifier())
                        .setDefaultRequestConfig(RequestConfig.custom()
                                .setConnectTimeout(TIMEOUT)
                                .setSocketTimeout(TIMEOUT)
                                .setConnectionRequestTimeout(TIMEOUT)
                                .build())
                        .setMaxConnPerRoute(2000)
                        .setMaxConnTotal(5000)
                        .build()))
                .build();
    }
}
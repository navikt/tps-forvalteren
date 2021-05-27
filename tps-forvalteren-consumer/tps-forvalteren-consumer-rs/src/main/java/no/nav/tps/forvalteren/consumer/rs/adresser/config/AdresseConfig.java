package no.nav.tps.forvalteren.consumer.rs.adresser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.tps.forvalteren.consumer.rs.adresser.AdresseServiceConsumer;
import no.nav.tps.forvalteren.consumer.rs.config.credentials.AdresseServiceProperties;
import no.nav.tps.forvalteren.consumer.rs.config.credentials.AzureClientCredentials;
import no.nav.tps.forvalteren.consumer.rs.token.AccessTokenService;
import no.nav.tps.forvalteren.consumer.rs.token.InsecureAuthenticationTokenResolver;

@Configuration
public class AdresseConfig {

    @Value("${http.proxy:#{null}}")
    private String proxyHost;

    private
    @Value("${AAD_ISSUER_URI}") String issuerUrl;

    @Value("${azure.app.client.id:#{null}}")
    private String clientId;

    @Value("${azure.app.client.secret:#{null}}")
    private String clientSecret;

    @Value("${consumers.testnav-adresse-service.url}")
    private String url;

    @Value("${consumers.testnav-adresse-service.name}")
    private String name;

    @Value("${consumers.testnav-adresse-service.cluster}")
    private String cluster;

    @Value("${consumers.testnav-adresse-service.namespace}")
    private String namespace;

    @Bean
    public AzureClientCredentials clientCredentials(){
        return new AzureClientCredentials(clientId, clientSecret);
    }

    @Bean
    public AccessTokenService accessTokenService(){
        return new AccessTokenService(proxyHost, issuerUrl,
                new InsecureAuthenticationTokenResolver(),
                clientCredentials());
    }

    @Bean AdresseServiceConsumer adresseServiceConsumer() {
        return new AdresseServiceConsumer(AdresseServiceProperties.builder()
                .name(name)
                .cluster(cluster)
                .namespace(namespace)
                .url(url)
                .build(),
                accessTokenService());
    }
}

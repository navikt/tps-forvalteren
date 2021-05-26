package no.nav.tps.forvalteren.consumer.rs.config.credentials;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "consumers.testnav-adresse-service")
public class AdresseServiceProperties extends NaisServerProperties {
}

package no.nav.tps.forvalteren;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.vault.annotation.VaultPropertySource;

@Configuration
@Profile("local")
@VaultPropertySource(value = "kv/preprod/fss/tps-forvalteren/local", ignoreSecretNotFound = false)
@VaultPropertySource(value = "oracle/dev/creds/tpsforvalteren_t1-user", propertyNamePrefix = "spring.datasource.", ignoreSecretNotFound = false)
@VaultPropertySource(value = "oracle/dev/config/tpsforvalteren_t1", propertyNamePrefix = "tps-forvalteren.datasource.", ignoreSecretNotFound = false)
@VaultPropertySource(value = "serviceuser/test/srvtps-forvalteren", propertyNamePrefix = "srvtps.forvalteren.", ignoreSecretNotFound = false)
@VaultPropertySource(value = "azuread/prod/creds/team-dolly-lokal-app")

class VaultConfig {

}
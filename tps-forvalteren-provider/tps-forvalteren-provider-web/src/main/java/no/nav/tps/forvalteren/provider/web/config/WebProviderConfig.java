package no.nav.tps.forvalteren.provider.web.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.tps.forvalteren.common.config.CommonConfig;
import no.nav.tps.forvalteren.provider.rs.config.RestProviderConfig;

@Configuration
@EnableConfigurationProperties
@Import({ RestProviderConfig.class,
        CommonConfig.class,
})
public class WebProviderConfig {

}

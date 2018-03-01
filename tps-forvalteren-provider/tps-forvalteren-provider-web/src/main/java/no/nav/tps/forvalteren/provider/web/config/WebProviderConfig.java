package no.nav.tps.forvalteren.provider.web.config;

import no.nav.tps.forvalteren.common.java.config.CommonConfig;
import no.nav.tps.forvalteren.provider.rs.config.RestProviderConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;


@Configuration
@EnableConfigurationProperties
@Import({RestProviderConfig.class,
        CommonConfig.class,
})
public class WebProviderConfig {

}

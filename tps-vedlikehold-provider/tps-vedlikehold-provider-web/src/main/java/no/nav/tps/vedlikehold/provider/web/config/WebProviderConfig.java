package no.nav.tps.vedlikehold.provider.web.config;

import no.nav.tps.vedlikehold.common.java.config.CommonConfig;
import no.nav.tps.vedlikehold.provider.rs.config.RestProviderConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;


@Configuration
@EnableConfigurationProperties
@PropertySource("classpath:global.properties")
@Import({RestProviderConfig.class,
        CommonConfig.class,
})
public class WebProviderConfig {

}

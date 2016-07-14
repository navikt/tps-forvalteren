package no.nav.tps.vedlikehold.provider.web.config;
import no.nav.tps.vedlikehold.common.java.message.DefaultMessageProvider;
import no.nav.tps.vedlikehold.provider.web.SelftestController;

import no.nav.tps.vedlikehold.provider.web.selftest.DiskresjonskodeSelftest;
import no.nav.tps.vedlikehold.provider.web.selftest.EgenAnsattSelftest;
import no.nav.tps.vedlikehold.provider.web.selftest.Selftest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Configuration
@EnableConfigurationProperties
@PropertySource("classpath:global.properties")
public class WebProviderConfig {
    @Bean
    SelftestController selftestController() {
        return new SelftestController();
    }

    @Bean(name = "egenAnsattSelftest")
    Selftest egenAnsattselftest() {
        return new EgenAnsattSelftest();
    }

    @Bean(name = "diskresjonskodeSelftest")
    Selftest diskresjonskodeSelftest() {
        return new DiskresjonskodeSelftest();
    }

    @Bean
    DefaultMessageProvider defaultMessageProvider() {
        return new DefaultMessageProvider();
    }
}

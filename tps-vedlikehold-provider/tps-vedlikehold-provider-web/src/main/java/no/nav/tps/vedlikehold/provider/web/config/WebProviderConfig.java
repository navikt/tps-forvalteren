package no.nav.tps.vedlikehold.provider.web.config;
import no.nav.tps.vedlikehold.common.java.message.DefaultMessageProvider;
import no.nav.tps.vedlikehold.provider.web.SelfTestController;

import no.nav.tps.vedlikehold.provider.web.selftest.DiskresjonskodeSelftest;
import no.nav.tps.vedlikehold.provider.web.selftest.EgenAnsattSelftest;
import no.nav.tps.vedlikehold.provider.web.selftest.Selftest;
import no.nav.tps.vedlikehold.provider.web.selftest.VeraSelftest;
import no.nav.tps.vedlikehold.service.command.vera.PingVera;
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
    SelfTestController selftestController() {
        return new SelfTestController();
    }

    @Bean(name = "egenAnsattSelftest")
    Selftest egenAnsattselftest() {
        return new EgenAnsattSelftest();
    }

    @Bean(name = "diskresjonskodeSelftest")
    Selftest diskresjonskodeSelftest() {
        return new DiskresjonskodeSelftest();
    }

    @Bean(name = "veraSelftest")
    Selftest veraSelftest() {
        return new VeraSelftest();
    }

    @Bean
    PingVera pingVera() {
        return new PingVera();
    }

    @Bean
    DefaultMessageProvider defaultMessageProvider() {
        return new DefaultMessageProvider();
    }
}

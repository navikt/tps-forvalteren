package no.nav.tps.vedlikehold.provider.web.config;
import no.nav.tps.vedlikehold.common.java.message.DefaultMessageProvider;
import no.nav.tps.vedlikehold.provider.web.SelftestController;

import no.nav.tps.vedlikehold.provider.web.selftest.*;
import no.nav.tps.vedlikehold.service.command.fasit.PingFasit;
import no.nav.tps.vedlikehold.service.command.mq.PingMq;
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

    @Bean(name = "veraSelftest")
    Selftest veraSelftest() {
        return new VeraSelftest();
    }

    @Bean(name = "fasitSelftest")
    Selftest fasitSelftest() {
        return new FasitSelftest();
    }

    @Bean(name = "mqSelftest")
    Selftest mqSelftest() {
        return new MqSelftest();
    }

    //TODO: check if they can be instantiated in common config
    @Bean
    PingVera pingVera() {
        return new PingVera();
    }

    @Bean
    PingFasit pingFasit() {
        return new PingFasit();
    }

    @Bean
    PingMq pingMq() {
        return new PingMq();
    }

    @Bean
    DefaultMessageProvider defaultMessageProvider() {
        return new DefaultMessageProvider();
    }
}

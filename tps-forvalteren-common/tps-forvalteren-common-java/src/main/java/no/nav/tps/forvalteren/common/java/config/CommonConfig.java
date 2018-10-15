package no.nav.tps.forvalteren.common.java.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.tps.forvalteren.common.java.mapping.MapperConfig;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;


@Configuration
@ComponentScan(basePackageClasses = {
        MessageProvider.class
})
@Import(MapperConfig.class)
public class CommonConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
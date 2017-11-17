package no.nav.tps.forvalteren.common.java.config;

import no.nav.tps.forvalteren.common.java.mapping.MapperConfig;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@ComponentScan(basePackageClasses = {
        MessageProvider.class
})
@Import(MapperConfig.class)
public class CommonConfig {
}

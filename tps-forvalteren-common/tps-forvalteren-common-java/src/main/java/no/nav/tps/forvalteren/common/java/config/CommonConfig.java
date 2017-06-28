package no.nav.tps.forvalteren.common.java.config;

import no.nav.tps.forvalteren.common.java.mapping.MapperConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@ComponentScan( value = "no.nav.tps.forvalteren.common.java.message" )
@Import(MapperConfig.class)
public class CommonConfig {
}

package no.nav.tps.forvalteren.service.kodeverk.config;

import no.nav.tps.forvalteren.service.kodeverk.KodeverkCache;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = KodeverkCache.class)
public class KodeverkConfig {
}

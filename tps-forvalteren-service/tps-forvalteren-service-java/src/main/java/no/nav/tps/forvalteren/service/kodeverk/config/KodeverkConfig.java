package no.nav.tps.forvalteren.service.kodeverk.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import no.nav.tps.forvalteren.service.kodeverk.KodeverkCache;

@Configuration
@ComponentScan(basePackageClasses = KodeverkCache.class)
public class KodeverkConfig {
}

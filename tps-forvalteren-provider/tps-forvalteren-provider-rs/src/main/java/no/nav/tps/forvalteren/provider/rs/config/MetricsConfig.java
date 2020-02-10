package no.nav.tps.forvalteren.provider.rs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import no.nav.freg.metrics.spring.config.MetricsSpringConfig;
import no.nav.freg.metrics.spring.config.MetricsSpringSensuConfig;
import no.nav.freg.metrics.spring.config.MetricsSpringTagSupplierConfig;

@Profile("remote")
@Configuration
@Import({
        MetricsSpringConfig.class,
        MetricsSpringSensuConfig.class,
        MetricsSpringTagSupplierConfig.class
})
public class MetricsConfig {}


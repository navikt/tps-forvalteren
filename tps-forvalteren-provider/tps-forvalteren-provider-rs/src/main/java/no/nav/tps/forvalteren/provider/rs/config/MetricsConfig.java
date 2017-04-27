package no.nav.tps.forvalteren.provider.rs.config;

import no.nav.freg.metrics.spring.config.MetricsSpringConfig;
import no.nav.freg.metrics.spring.config.MetricsSpringSensuConfig;
import no.nav.freg.metrics.spring.config.MetricsSpringTagSupplierConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Profile("remote")
@Configuration
@Import({
        MetricsSpringConfig.class,
        MetricsSpringSensuConfig.class,
        MetricsSpringTagSupplierConfig.class
})
public class MetricsConfig {}


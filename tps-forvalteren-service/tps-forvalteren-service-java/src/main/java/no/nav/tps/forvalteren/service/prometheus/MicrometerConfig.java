package no.nav.tps.forvalteren.service.prometheus;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.spring.autoconfigure.MeterRegistryCustomizer;
import no.nav.tps.forvalteren.service.prometheus.timed.TimedMethodInterceptor;
import no.nav.tps.forvalteren.service.prometheus.timed.TimedPointcutAdvisor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> commonTags() {
        return registry -> registry.config().commonTags("app", "tps-forvalteren", "team", "freg");
    }

    @Bean
    public TimedPointcutAdvisor timedPointcutAdvisor() {
        return new TimedPointcutAdvisor();
    }

    @Bean
    public TimedMethodInterceptor timedMethodInterceptor() {
        return new TimedMethodInterceptor();
    }
}

package no.nav.tps.forvalteren.service.prometheus;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;
import no.nav.tps.forvalteren.service.prometheus.timed.TimedMethodInterceptor;
import no.nav.tps.forvalteren.service.prometheus.timed.TimedPointcutAdvisor;

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

//    @Bean
//    InitializingBean prometheusBeanPostProcessor(BeanPostProcessor meterRegistryPostProcessor, PrometheusMeterRegistry prometheusMeterRegistry) {
//        return () -> meterRegistryPostProcessor.postProcessAfterInitialization(prometheusMeterRegistry, "");
//    }
}

package no.nav.tps.forvalteren.service.prometheus.timed;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class TimedMethodInterceptor implements MethodInterceptor {

    @Autowired
    private ApplicationContext context;

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        MeterRegistry registry = context.getBean(MeterRegistry.class);
        Timer.Sample sample = Timer.start(registry);

        try {
            return mi.proceed();
        } finally {
            Timed timed = findAnnotation(mi);
            sample.stop(registry.timer(timed.name(), timed.tags()));
        }
    }

    private Timed findAnnotation(MethodInvocation mi) {
        return mi.getMethod().getAnnotation(Timed.class);
    }
}

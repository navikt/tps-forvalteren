package no.nav.tps.forvalteren.service.prometheus.timed;

import java.lang.reflect.Method;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;

public class TimedPointcutAdvisor extends AbstractPointcutAdvisor {

    private static final StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
        @Override
        public boolean matches(Method method, Class<?> aClass) {
            return method.isAnnotationPresent(Timed.class);
        }
    };

    @Autowired
    private TimedMethodInterceptor interceptor;

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.interceptor;
    }
}

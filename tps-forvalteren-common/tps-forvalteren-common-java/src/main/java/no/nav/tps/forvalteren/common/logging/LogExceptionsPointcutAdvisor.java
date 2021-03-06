package no.nav.tps.forvalteren.common.logging;

import java.lang.reflect.Method;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogExceptionsPointcutAdvisor extends AbstractPointcutAdvisor {

    private static final StaticMethodMatcherPointcut POINTCUT = new StaticMethodMatcherPointcut() {
        @Override
        public boolean matches(Method method, Class<?> aClass) {
            return method.isAnnotationPresent(LogExceptions.class);
        }
    };

    @Autowired
    private LogExceptionsMethodInterceptor interceptorAdvice;

    @Override
    public Pointcut getPointcut() {
        return POINTCUT;
    }

    @Override
    public Advice getAdvice() {
        return interceptorAdvice;
    }
}
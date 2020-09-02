package no.nav.tps.forvalteren.common.java.logging;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;

public class LogExceptionsMethodInterceptor implements MethodInterceptor {
    private final Logger exceptionLogger;
    private final Logger functionalLogger;

    public LogExceptionsMethodInterceptor(Logger exceptionLogger, Logger functionalLogger) { // NOSONAR - Sonar complains about taking a exceptionLogger as parameter.
        this.exceptionLogger = exceptionLogger;
        this.functionalLogger = functionalLogger;
    }

    @Override
    // Throwable can be thrown by methodInvocation.proceed(), and therefore it has to be thrown here.
    // The NOSONAR comment tells SonarQube to ignore that line.
    public Object invoke(MethodInvocation mi) throws Throwable { // NOSONAR
        try {
            return mi.proceed();
        } catch (Throwable e) { // NOSONAR - Thrown by interface
            LogExceptions logExceptions = findAnnotation(mi);
            logException(logExceptions, e);
            throw e;
        }
    }

    private LogExceptions findAnnotation(MethodInvocation mi) {
        return mi.getMethod().getAnnotation(LogExceptions.class);
    }

    private void logException(LogExceptions le, Throwable e) {
        if (arrayContainsExceptionType(le.functional(), e)) {
            functionalLogger.error(e.getClass().getSimpleName() + " - " + e.getMessage()); // NOSONAR - Only need general info, not stacktrace
        } else if (!arrayContainsExceptionType(le.ignored(), e)) {
            exceptionLogger.error(e.getMessage(), e);
        }
    }

    private boolean arrayContainsExceptionType(Class<?>[] classes, Throwable e) {
        for (Class<?> clazz : classes) {
            if (clazz.isAssignableFrom(e.getClass())) {
                return true;
            }
        }
        return false;
    }
}
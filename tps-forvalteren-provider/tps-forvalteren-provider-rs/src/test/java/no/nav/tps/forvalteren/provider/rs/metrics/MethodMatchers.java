package no.nav.tps.forvalteren.provider.rs.metrics;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class MethodMatchers {

    static BaseMatcher<Method> hasAnnotation(final Class<? extends Annotation> annotationClass) {
        return new BaseMatcher<Method>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Missing annotation: " + annotationClass.getSimpleName());
            }

            @Override
            public boolean matches(Object o) {
                return ((Method)o).getAnnotation(annotationClass) != null;
            }
        };
    }
}

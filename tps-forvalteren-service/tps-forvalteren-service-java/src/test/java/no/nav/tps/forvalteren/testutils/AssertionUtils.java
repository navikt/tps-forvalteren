package no.nav.tps.forvalteren.testutils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AssertionUtils {

    public static <T> void assertAllFieldsNotNull(T objekt) throws InvocationTargetException, IllegalAccessException {
        List<Method> methods = Arrays.asList(objekt.getClass().getDeclaredMethods());

        List<Method> getters = methods.stream().filter(method -> "get".equals(method.getName().substring(0, 3))).collect(Collectors.toList());
        for (Method method : getters) {
            assertThat(method.getName() + " returned null", method.invoke(objekt), is(notNullValue()));
        }
        List<Method> methods_is = methods.stream().filter(method -> "is".equals(method.getName().substring(0, 2))).collect(Collectors.toList());
        for (Method method : methods_is) {
            assertThat(method.getName() + " returned null", method.invoke(objekt), is(notNullValue()));
        }
    }

    public static <T> void assertAllFieldsNotNull(T objekt, List<String> ignoreMethod) throws InvocationTargetException, IllegalAccessException {
        List<Method> methods = Arrays.asList(objekt.getClass().getDeclaredMethods());

        List<Method> getters = methods.stream().filter(method -> "get".equals(method.getName().substring(0, 3))).collect(Collectors.toList());
        for (Method method : getters) {
            if (!ignoreMethod.contains(method.getName())) {
                assertThat(method.getName() + " returned null", method.invoke(objekt), is(notNullValue()));
            }
        }
        List<Method> methods_is = methods.stream().filter(method -> "is".equals(method.getName().substring(0, 2))).collect(Collectors.toList());
        for (Method method : methods_is) {
            assertThat(method.getName() + " returned null", method.invoke(objekt), is(notNullValue()));
        }
    }
}

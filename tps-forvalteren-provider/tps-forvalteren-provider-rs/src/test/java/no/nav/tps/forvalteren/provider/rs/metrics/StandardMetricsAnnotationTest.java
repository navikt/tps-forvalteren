package no.nav.tps.forvalteren.provider.rs.metrics;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.EnvironmentController;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.GeografiskTilhoerighetController;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.ServiceroutineController;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.TpsServicesController;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.UserController;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.XmlMeldingController;

@RunWith(MockitoJUnitRunner.class)
public class StandardMetricsAnnotationTest {

    private static final Class<?>[] PROVIDERS = new Class[] {
            EnvironmentController.class,
            ServiceroutineController.class,
            UserController.class,
            TpsServicesController.class,
            GeografiskTilhoerighetController.class,
            XmlMeldingController.class
    };

    @Test
    public void checkPresenceOfAnnotationsOnProvidersRs(){
        for (Class<?> provider : PROVIDERS) {
            verifyRequiredAnnotationsForMethods(getMethods(provider));
        }
    }

    private List<Method> getMethods(Class<?> provider){
        List<Method> methods = new ArrayList<>();
        for(Method method : provider.getDeclaredMethods()){
            if(Modifier.isPublic(method.getModifiers())){
                methods.add(method);
            }
        }
        return methods;
    }

    private void verifyRequiredAnnotationsForMethods(List<Method> methods) {
        for (Method method : methods) {
            verifyRequiredAnnotations(method);
        }
    }

    private void verifyRequiredAnnotations(Method method) {
        MatcherAssert.assertThat(method, MethodMatchers.hasAnnotation(Metrics.class));
    }
}

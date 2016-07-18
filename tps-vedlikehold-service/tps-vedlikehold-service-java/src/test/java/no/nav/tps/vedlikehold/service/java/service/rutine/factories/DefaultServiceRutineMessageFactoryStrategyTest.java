package no.nav.tps.vedlikehold.service.java.service.rutine.factories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultServiceRutineMessageFactoryStrategyTest {

    private static final String SERVICE_RUTINE_PARAMETER_NAME = "serviceRutinenavn";
    private static final String SERVICE_RUTINE_NAME           = "serviceRutineName";


    @Test
    public void serviceRutineNameIsAddedWithTheCorrectPropertyName() {
        DefaultServiceRutineMessageFactoryStrategy strategy = new DefaultServiceRutineMessageFactoryStrategy(SERVICE_RUTINE_NAME, newMap());

        assertThat(strategy.getParameters().keySet(), containsInAnyOrder(SERVICE_RUTINE_PARAMETER_NAME));
        assertThat(strategy.getParameters().values(), containsInAnyOrder((Object) SERVICE_RUTINE_NAME));
    }

    @Test
    public void allPropertiesAndTheServiceRutineNameAreReturnedAsParameters() {
        Map<String, Object> parameters = newMap("parameter1", "value1", "parameter2", "value2");

        DefaultServiceRutineMessageFactoryStrategy strategy = new DefaultServiceRutineMessageFactoryStrategy(SERVICE_RUTINE_NAME, parameters);

        assertThat(strategy.getParameters().values(), hasSize(3));
        assertThat(strategy.getParameters().values(), containsInAnyOrder("value1", "value2", SERVICE_RUTINE_NAME));
        assertThat(strategy.getParameters().keySet(), containsInAnyOrder("parameter1", "parameter2", SERVICE_RUTINE_PARAMETER_NAME));
    }

    private Map<String, Object> newMap(Object... keyValues) {
        Map<String, Object> map = new HashMap<>();

        List<Object> keyValuesList = Arrays.asList(keyValues);

        for (Integer index = 0; index < keyValues.length; index += 2) {
            String key = (String) keyValuesList.get(index);
            Object value = keyValuesList.get(index+1);
            map.put( key, value );
        }

        return map;
    }

}

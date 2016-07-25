package no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hamcrest.core.StringEndsWith;
import org.hamcrest.core.StringStartsWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultServiceRutineMessageFactoryTest {

    private static final String XML_PARAMETERS_PREFIX  = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData><tpsServiceRutine>";
    private static final String XML_PARAMETERS_POSTFIX = "</tpsServiceRutine></tpsPersonData>";

    private static final Map<String, Object> PARAMETERS   = newMap("parameter1", "value1", "parameter2", "value2");
    private static final String XML_PARAMETERS_DEFINITION = "<parameter1>value1</parameter1><parameter2>value2</parameter2>";

    @Mock
    private ServiceRutineMessageFactoryStrategy messageFactoryStrategyMock;

    private DefaultServiceRutineMessageFactory defaultServiceRutineMessageFactory;


    @Before
    public void setUp() {
        this.defaultServiceRutineMessageFactory = new DefaultServiceRutineMessageFactory();

        when(messageFactoryStrategyMock.getParameters()).thenReturn(PARAMETERS);
    }


    @Test
    public void xmlDefinitionIsValid() {
        when(messageFactoryStrategyMock.getParameters()).thenReturn( newMap() );

        String result = defaultServiceRutineMessageFactory.createMessage(messageFactoryStrategyMock);

        assertThat(result, StringStartsWith.startsWith(XML_PARAMETERS_PREFIX));
        assertThat(result, StringEndsWith.endsWith(XML_PARAMETERS_POSTFIX));
    }

    @Test
    public void containsAllParameters() {
        String result = defaultServiceRutineMessageFactory.createMessage(messageFactoryStrategyMock);

        assertThat(result, containsString("<parameter1>value1</parameter1>"));
        assertThat(result, containsString("<parameter2>value2</parameter2>"));
    }

    @Test
    public void containsOnlyDefinitionAndParameters() {
        String result = defaultServiceRutineMessageFactory.createMessage(messageFactoryStrategyMock);

        assertThat(result.length(), is( XML_PARAMETERS_PREFIX.length() + XML_PARAMETERS_DEFINITION.length() + XML_PARAMETERS_POSTFIX.length() ));
    }

    private static Map<String, Object> newMap(Object... keyValues) {
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

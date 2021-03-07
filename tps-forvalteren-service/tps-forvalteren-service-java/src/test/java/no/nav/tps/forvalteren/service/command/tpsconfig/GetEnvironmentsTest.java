package no.nav.tps.forvalteren.service.command.tpsconfig;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.java.config.TpsPropertiesConfig;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;

@RunWith(MockitoJUnitRunner.class)
public class GetEnvironmentsTest {

    @Mock
    private TpsPropertiesConfig tpsProperties;

    @Mock
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @InjectMocks
    private GetEnvironments getEnvironments;

    @Before
    public  void setup() {

        when(tpsProperties.getEnvironments()).thenReturn(Collections.emptySet());
    }

    @Test
    public void callsGetEnvironmentsOnVeraConsumer() {
        getEnvironments.getEnvironments();

        verify(tpsProperties).getEnvironments();
        verify(filterEnvironmentsOnDeployedEnvironment).execute(anySet());
    }
}

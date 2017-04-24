package no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt;

import no.nav.tps.forvalteren.consumer.ws.tpsws.config.TpswsConsumerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EgenAnsattSelftestCheckTest {

    @InjectMocks
    private EgenAnsattSelftestCheck selftestCheck;

    @Mock
    private TpswsConsumerConfig config;

    @Mock
    private DefaultEgenAnsattConsumer consumer;

    private final String ENDPOINT_URL = "EGENANSATT_URL";

    @Before
    public void before() {
        when(config.getEgenAnsattAddress()).thenReturn(ENDPOINT_URL);
    }

    @Test
    public void includesTheApplicationNameInDescription() {
        String uppercaseResult = selftestCheck.getDescription().toUpperCase();
        assertThat(uppercaseResult, containsString("PIPEGENANSATT"));
    }

    @Test
    public void includesOperationNameInEndpoint() {
        String result = selftestCheck.getEndpoint();
        verify(config).getEgenAnsattAddress();
        assertThat(result, containsString("ping"));
    }

    @Test
    public void includesFasitAliasInEndpoint() {
        String result = selftestCheck.getEndpoint();
        verify(config).getEgenAnsattAddress();
        assertThat(result, containsString("virksomhet:PipEgenAnsatt_v1"));
    }

    @Test
    public void includesEndpointUrlInEndpoint() {
        String result = selftestCheck.getEndpoint();
        verify(config).getEgenAnsattAddress();
        assertThat(result, containsString(ENDPOINT_URL));
    }

    @Test
    public void isVital() {
        boolean result = selftestCheck.isVital();
        assertThat(result, is(true));
    }

    @Test
    public void returnsTrueOnPerform() {
        boolean result = selftestCheck.perform();
        assertThat(result, is(true));
    }


}

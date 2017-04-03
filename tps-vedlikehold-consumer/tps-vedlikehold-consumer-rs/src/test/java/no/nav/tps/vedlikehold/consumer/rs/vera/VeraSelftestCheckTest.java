package no.nav.tps.vedlikehold.consumer.rs.vera;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static no.nav.tps.vedlikehold.consumer.rs.vera.DefaultVeraConsumer.BASE_URL;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VeraSelftestCheckTest {

    @InjectMocks
    private VeraSelftestCheck selftestCheck;

    @Mock
    private VeraConsumer consumer;

    @Test
    public void includesTheApplicationNameInDescription() {
        String uppercaseResult = selftestCheck.getDescription().toUpperCase();
        assertThat(uppercaseResult, containsString("VERA"));
    }

    @Test
    public void includesOperationNameInEndpoint() {
        String result = selftestCheck.getEndpoint();
        assertThat(result, containsString("ping"));
    }

    @Test
    public void includesFasitAliasInEndpoint() {
        String result = selftestCheck.getEndpoint();
        assertThat(result, containsString("deployLog_v1"));
    }

    @Test
    public void includesEndpointUrlInEndpoint() {
        String result = selftestCheck.getEndpoint();
        assertThat(result, containsString(BASE_URL));
    }

    @Test
    public void isVital() {
        boolean result = selftestCheck.isVital();
        assertThat(result, is(false));
    }

    @Test
    public void returnsTrueOnPerform() {
        when(consumer.getEnvironments(anyString())).thenReturn(null);
        boolean result = selftestCheck.perform();
        assertThat(result, is(true));
    }

}

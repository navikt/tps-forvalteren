package no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.config.TpswsConsumerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiskresjonskodeSelftestCheckTest {

    @InjectMocks
    private DiskresjonskodeSelftestCheck selftestCheck;

    @Mock
    private TpswsConsumerConfig config;

    @Mock
    private DiskresjonskodeConsumer consumer;

    private final String ENDPOINT_URL = "DISKRESJONSKODE_URL";

    @Before
    public void before() {
        when(config.getDiskresjonskodeAddress()).thenReturn(ENDPOINT_URL);
    }

    @Test
    public void includesTheApplicationNameInDescription() {
        String uppercaseResult = selftestCheck.getDescription().toUpperCase();
        assertThat(uppercaseResult, containsString("DISKRESJONSKODE"));
    }

    @Test
    public void includesOperationNameInEndpoint() {
        String result = selftestCheck.getEndpoint();
        verify(config).getDiskresjonskodeAddress();
        assertThat(result, containsString("ping"));
    }

    @Test
    public void includesFasitAliasInEndpoint() {
        String result = selftestCheck.getEndpoint();
        verify(config).getDiskresjonskodeAddress();
        assertThat(result, containsString("virksomhet:Diskresjonskode_v1"));
    }

    @Test
    public void includesEndpointUrlInEndpoint() {
        String result = selftestCheck.getEndpoint();
        verify(config).getDiskresjonskodeAddress();
        assertThat(result, containsString(ENDPOINT_URL));
    }

    @Test
    public void isVital() {
        boolean result = selftestCheck.isVital();
        assertThat(result, is(true));
    }

    @Test
    public void returnsTrueOnPerform() {
        when(consumer.getDiskresjonskodeResponse(anyString())).thenReturn(null);
        boolean result = selftestCheck.perform();
        assertThat(result, is(true));
    }

}

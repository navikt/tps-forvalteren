package no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiskresjonskodeSelftestCheckTest {

    @InjectMocks
    private DiskresjonskodeSelftestCheck selftestCheck;

    @Mock
    private TpswsConsumerConfig properties;

    @Mock
    private DiskresjonskodeConsumer consumer;

    private final String ENDPOINT_URL = "DISKRESJONSKODE_URL";
    private final String FNR_TEST_PERSON_IPROD = "10108000398";

    @Before
    public void before() {
        when(properties.getDiskresjonskodeAddress()).thenReturn(ENDPOINT_URL);
        when(consumer.getDiskresjonskodeResponse(FNR_TEST_PERSON_IPROD)).thenReturn(new HentDiskresjonskodeResponse());
    }

    @Test
    public void includesTheApplicationNameInDescription() {
        String uppercaseResult = selftestCheck.getDescription().toUpperCase();
        assertThat(uppercaseResult, containsString("DISKRESJONSKODE"));
    }

    @Test
    public void includesOperationNameInEndpoint() {
        String result = selftestCheck.getEndpoint();
        verify(properties).getDiskresjonskodeAddress();
        assertThat(result, containsString("ping"));
    }

    @Test
    public void includesFasitAliasInEndpoint() {
        String result = selftestCheck.getEndpoint();
        verify(properties).getDiskresjonskodeAddress();
        assertThat(result, containsString("virksomhet:Diskresjonskode_v1"));
    }

    @Test
    public void includesEndpointUrlInEndpoint() {
        String result = selftestCheck.getEndpoint();
        verify(properties).getDiskresjonskodeAddress();
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

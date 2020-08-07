package no.nav.tps.forvalteren.consumer.ws.tpsws.cxf;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class TimeoutFeatureTest {

    private final long RECEIVE_TIMEOUT = 20000;
    private final long CONNECTION_TIMEOUT_MIN_VALUE_DEFAULT = 30000L;
    private final long CONNECTION_TIMEOUT = 4000;

    @Mock
    private HTTPConduit httpConduit;

    @Mock
    private Conduit conduit;

    @Mock
    private Client client;

    @Test
    public void setsHttpClientPolicyForHttpConduit() {
        when(client.getConduit()).thenReturn(httpConduit);

        new TimeoutFeature(RECEIVE_TIMEOUT).initialize(client, null);

        verify(httpConduit).setClient(any(HTTPClientPolicy.class));
    }

    @Test
    public void doesNotSetHttpClientPolicyForOtherConduits() {
        when(client.getConduit()).thenReturn(conduit);

        new TimeoutFeature(RECEIVE_TIMEOUT).initialize(client, null);

        verifyNoInteractions(conduit);
    }

    @Test
    public void setsConnectionTimeoutWhenConnectionTimeoutIsNotEqualToLongMin() {
        when(client.getConduit()).thenReturn(httpConduit);

        new TimeoutFeature(RECEIVE_TIMEOUT, CONNECTION_TIMEOUT).initialize(client, null);

        ArgumentCaptor<HTTPClientPolicy> captor = ArgumentCaptor.forClass(HTTPClientPolicy.class);
        verify(httpConduit).setClient(captor.capture());

        assertThat(captor.getValue().getConnectionTimeout(), equalTo(CONNECTION_TIMEOUT));
    }

    @Test
    public void doesNotSetConnectionTimeoutWhenConnectionTimeoutIsEqualToLongMin() {
        when(client.getConduit()).thenReturn(httpConduit);

        new TimeoutFeature(RECEIVE_TIMEOUT).initialize(client, null);

        ArgumentCaptor<HTTPClientPolicy> captor = ArgumentCaptor.forClass(HTTPClientPolicy.class);
        verify(httpConduit).setClient(captor.capture());

        assertThat(captor.getValue().getConnectionTimeout(), equalTo(CONNECTION_TIMEOUT_MIN_VALUE_DEFAULT));
    }
}

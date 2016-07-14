package no.nav.tps.vedlikehold.consumer.ws.tpsws.cfx;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.cxf.TimeoutFeature;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class TimeoutFeatureTest {

    @Mock
    private Client client;

    @Test
    public void setsHttpClientPolicyForHttpConduit() {
        HTTPConduit httpConduit = mock(HTTPConduit.class);
        when(client.getConduit()).thenReturn(httpConduit);

        new TimeoutFeature(1000).initialize(client, null);

        verify(httpConduit).setClient(any(HTTPClientPolicy.class));
    }

    @Test
    public void doesNotSetHttpClientPolicyForOtherConduits() {
        Conduit conduit = mock(Conduit.class);
        when(client.getConduit()).thenReturn(conduit);

        new TimeoutFeature(1000).initialize(client, null);
        verifyZeroInteractions(conduit);
    }
}

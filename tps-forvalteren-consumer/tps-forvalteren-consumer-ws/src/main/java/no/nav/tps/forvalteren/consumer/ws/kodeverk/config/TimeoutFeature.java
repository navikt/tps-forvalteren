package no.nav.tps.forvalteren.consumer.ws.kodeverk.config;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

/**
 * @author Robin Tordly, Visma Consulting AS
 */
public class TimeoutFeature extends AbstractFeature {

    private long timeout;

    public TimeoutFeature(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void initialize(Client client, Bus bus) {
        Conduit conduit = client.getConduit();
        if (conduit instanceof HTTPConduit) {
            HTTPClientPolicy policy = new HTTPClientPolicy();
            policy.setReceiveTimeout(timeout);
            HTTPConduit httpConduit = (HTTPConduit) conduit;
            httpConduit.setClient(policy);
        }
    }
}

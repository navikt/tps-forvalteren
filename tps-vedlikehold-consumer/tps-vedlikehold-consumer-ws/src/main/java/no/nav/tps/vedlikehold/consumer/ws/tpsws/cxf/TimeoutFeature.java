package no.nav.tps.vedlikehold.consumer.ws.tpsws.cxf;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public class TimeoutFeature extends AbstractFeature {
    private long receiveTimeout;
    private long connectionTimeout = Long.MIN_VALUE;

    public TimeoutFeature(long receiveTimeout) {
        super();

        this.receiveTimeout = receiveTimeout;
    }

    public TimeoutFeature(long receiveTimeout, long connectionTimeout) {
        super();
        
        this.receiveTimeout = receiveTimeout;
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public void initialize(Client client, Bus bus) {
        Conduit conduit = client.getConduit();

        if (conduit instanceof HTTPConduit) {
            HTTPConduit httpConduit = (HTTPConduit) conduit;
            HTTPClientPolicy policy = new HTTPClientPolicy();

            policy.setReceiveTimeout(receiveTimeout);

            if (connectionTimeout != Long.MIN_VALUE) {
                policy.setConnectionTimeout(connectionTimeout);
            }

            httpConduit.setClient(policy);
        }

        super.initialize(client, bus);
    }
}
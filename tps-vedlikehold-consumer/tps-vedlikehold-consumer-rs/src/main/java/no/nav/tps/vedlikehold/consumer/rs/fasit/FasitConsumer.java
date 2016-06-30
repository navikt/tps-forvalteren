package no.nav.tps.vedlikehold.consumer.rs.fasit;

import no.nav.brevogarkiv.common.fasit.FasitClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Connects to the TPSWS application's Fasit resources
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Service
public class FasitConsumer {
    //TODO: Could be configured in fasit/applicationproperties?
    private static final String APPLICATION_NAME        = "tpsws";
    private static final String MESSAGE_QUEUE_REQUEST   = "tps.endrings.melding";
    private static final String MESSAGE_QUEUE_RESPONSE  = "tps.endrings.melding.svar";

    @Autowired
    private FasitClient fasitClient;

    public FasitClient.Application getApplication(String environment) {
        return fasitClient.getApplication(environment, APPLICATION_NAME);
    }

    public String getRequestQueue(String environment) {
        return getApplication(environment).getQueue( MESSAGE_QUEUE_REQUEST );
    }

    public String getResponseQueue(String environment) {
        return getApplication(environment).getQueue( MESSAGE_QUEUE_RESPONSE );
    }

}

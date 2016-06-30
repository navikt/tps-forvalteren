package no.nav.tps.vedlikehold.consumer.rs.fasit;

import no.nav.brevogarkiv.common.fasit.FasitClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Service
public class FasitConsumer {

    private static final String APPLICATION_NAME = "tps-vedlikehold";

    @Autowired
    private FasitClient fasitClient;

    public FasitClient.Application getApplication(String environment) {
        return fasitClient.getApplication(environment, APPLICATION_NAME);
    }

}

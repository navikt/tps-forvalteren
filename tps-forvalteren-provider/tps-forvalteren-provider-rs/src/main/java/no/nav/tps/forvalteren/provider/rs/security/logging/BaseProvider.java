package no.nav.tps.forvalteren.provider.rs.security.logging;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseProvider {

    private static final Logger SPORINGSLOGGER = LoggerFactory.getLogger("no.nav.tps.forvalteren.provider.rs.Sporingslogger");

    protected void loggSporing(String serviceRutine,  Map<String, Object> requestParameters) {

        if (SPORINGSLOGGER.isInfoEnabled()) {
            SPORINGSLOGGER.info("ServiceRutine: {}, Request: {}",
                    serviceRutine,
                    requestParameters
            );
        }
    }
}


package no.nav.tps.vedlikehold.provider.rs.security.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Sporingslogger {

    private static final String NAME = "no.nav.tps.vedlikehold.provider.rs.Sporingslogger";
    private static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static void loggSporing(String serviceRutine,  Map<String, Object> requestParameters) {
        LOGGER.info("ServiceRutine: {}, Request: {}", serviceRutine, requestParameters.toString());
    }

}

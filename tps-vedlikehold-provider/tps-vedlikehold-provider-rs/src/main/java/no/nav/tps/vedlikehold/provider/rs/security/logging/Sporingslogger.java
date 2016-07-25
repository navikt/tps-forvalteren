package no.nav.tps.vedlikehold.provider.rs.security.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class Sporingslogger {

    private static final String NAME = "no.nav.tps.vedlikehold.provider.rs.Sporingslogger";
    private static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static void log(String environment, String serviceRutine, String fnr) {
        LOGGER.info("personnummer: {}, serviceRutine: {}, environment: {}",
                fnr,
                serviceRutine,
                environment);
    }
}

package no.nav.tps.vedlikehold.provider.rs.security.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class Sporingslogger {
    private static final Logger SPORINGSLOGGER = LoggerFactory.getLogger("no.nav.tps.vedlikehold.provider.rs.Sporingslogger");

    public static void log(String environment, String serviceRutine, String fnr) {
        SPORINGSLOGGER.info("personnummer: {}, serviceRutine: {}, environment: {}",
                fnr,
                serviceRutine,
                environment);
    }
}

package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class BaseController {
    private static final Logger SPORINGSLOGGER = LoggerFactory.getLogger("no.nav.tps.vedlikehold.provider.rs.Sporingslogger");

    protected void loggSporing(String environment, String serviceRutine, String fnr) {
        SPORINGSLOGGER.info("personnummer: {}, serviceRutine: {}, environment: {}",
                fnr,
                serviceRutine,
                environment);
    }
}

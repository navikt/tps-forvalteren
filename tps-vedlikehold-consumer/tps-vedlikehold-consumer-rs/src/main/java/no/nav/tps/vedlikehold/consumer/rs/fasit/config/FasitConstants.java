package no.nav.tps.vedlikehold.consumer.rs.fasit.config;

/**
 * Extracted Fasit constants to simplify the process of connecting to new queues and managers, when they are defined
 */
public class FasitConstants {
    static final public String BASE_URL = "https://fasit.adeo.no/conf/";
    static final String USERNAME = "admin";
    static final String PASSWORD = "admin"; // NOSONAR

    /* The name of the fasit application */
    static final String APPLICATION_NAME = "tpsws";

    /* Aliases for the different resources. Should be the same in all environments */
    static final String QUEUE_MANAGER_ALIAS  = "mqGateway";
    static final String RESPONSE_QUEUE_ALIAS = "tps.endrings.melding.svar";
}

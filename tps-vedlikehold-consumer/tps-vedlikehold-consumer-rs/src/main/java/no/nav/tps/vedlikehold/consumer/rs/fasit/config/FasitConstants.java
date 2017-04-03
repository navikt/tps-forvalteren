package no.nav.tps.vedlikehold.consumer.rs.fasit.config;

/**
 * Extracted Fasit constants to simplify the process of connecting to new queues and managers, when they are defined
 */
public class FasitConstants {
    public static final String BASE_URL = "https://fasit.adeo.no/conf/";
    protected static final String USERNAME = "admin";
    protected static final String PASSWORD = "admin"; // NOSONAR

    /* The name of the fasit application */
    protected static final String APPLICATION_NAME = "tpsws";

    /* Aliases for the different resources. Should be the same in all environments */
    protected static final String QUEUE_MANAGER_ALIAS  = "mqGateway";
    protected static final String RESPONSE_QUEUE_ALIAS = "tps.endrings.melding.svar";
}

package no.nav.tps.forvalteren.consumer.rs.fasit.config;

/**
 * Extracted Fasit constants to simplify the process of connecting to new queues and managers, when they are defined
 */
public class FasitConstants {

    /* The name of the fasit application */
    public static final String TPSF_FASIT_APP_NAME = "tps-forvalteren";

    /* Aliases for the different resources. Should be the same in all environments */
    public static final String QUEUE_MANAGER_ALIAS  = "mqGateway";
}

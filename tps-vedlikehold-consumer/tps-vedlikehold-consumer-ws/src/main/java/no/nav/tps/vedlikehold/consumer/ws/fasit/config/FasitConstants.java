package no.nav.tps.vedlikehold.consumer.ws.fasit.config;

/**
 * Extracted Fasit constants to simplify the process of connecting to new queues and managers, when they are defined
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */
class FasitConstants {
    static final String BASE_URL = "https://fasit.adeo.no/conf/";
    static final String USERNAME = "admin";
    static final String PASSWORD = "admin"; // NOSONAR

    /* The name of the fasit application */
    static final String APPLICATION_NAME = "tpsws";

    /* Aliases for the different resources. Should be the same in all environments */
    static final String QUEUE_MANAGER_ALIAS  = "mqGateway";
    static final String REQUEST_QUEUE_ALIAS  = "TPS_FORESPORSEL_XML_O";
    static final String RESPONSE_QUEUE_ALIAS = "tps.endrings.melding.svar";
}

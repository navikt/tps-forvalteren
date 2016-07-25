package no.nav.tps.vedlikehold.consumer.ws.fasit;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class FasitConstants {
    public static final String BASE_URL = "https://fasit.adeo.no/conf/";
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin";

    public static final String APPLICATION_NAME = "tpsws";

    public static final String QUEUE_MANAGER_ALIAS  = "mqGateway";
    public static final String REQUEST_QUEUE_ALIAS  = "TPS_FORESPORSEL_XML_O";
    public static final String RESPONSE_QUEUE_ALIAS = "tps.endrings.melding.svar";
}

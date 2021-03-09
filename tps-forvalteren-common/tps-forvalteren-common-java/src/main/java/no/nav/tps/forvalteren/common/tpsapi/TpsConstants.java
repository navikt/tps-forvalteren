package no.nav.tps.forvalteren.common.tpsapi;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TpsConstants {

    public static final String REQUEST_QUEUE_SERVICE_RUTINE_ALIAS = "TPS_FORESPORSEL_XML_O";
    public static final String REQUEST_QUEUE_ENDRINGSMELDING_ALIAS = "SFE_ENDRINGSMELDING";
    public static final String XML_REQUEST_QUEUE_SERVICE_RUTINE_ALIAS = "411.TPS_FORESPORSEL_XML_O";
    public static final String XML_REQUEST_QUEUE_ENDRINGSMELDING_ALIAS = "412.SFE_ENDRINGSMELDING";
    public static final String CHANNEL_POSTFIX = "_TPSF";
    public static final String TPSF_KILDE = "TPSF";

    protected static final String DEV_ENVIRONMENT = "D8";
    protected static final String PREFIX_MQ_QUEUES = "QA.";
    protected static final String MID_PREFIX_QUEUE_ENDRING = "_412.";
    protected static final String MID_PREFIX_QUEUE_HENTING = "_411.";
    protected static final String ZONE = "FSS";
    protected static final String FASIT_APP_NAME = "dummy";
    protected static final String QUEUE_MANAGER_ALIAS = "mqGateway";
}

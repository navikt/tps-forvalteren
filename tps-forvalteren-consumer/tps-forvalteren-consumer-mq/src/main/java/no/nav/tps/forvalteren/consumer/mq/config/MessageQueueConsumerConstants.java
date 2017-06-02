package no.nav.tps.forvalteren.consumer.mq.config;

public class MessageQueueConsumerConstants {

    /* Used to connect to the QueueManager */
    public static final String USERNAME = "srvappserver";
    public static final String PASSWORD = ""; // NOSONAR

    /* Used to create a channel name by combining it with the environment name. E.g. 'T1_TPSWS'*/
    public static final String CHANNEL_POSTFIX = "_TPSWS";
}
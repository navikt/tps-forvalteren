package no.nav.tps.forvalteren.common.java.config;

import java.util.List;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;
import no.nav.tps.forvalteren.common.java.util.Queue;
import no.nav.tps.forvalteren.common.java.util.QueueManager;
import no.nav.tps.forvalteren.common.java.util.TpsConstants;
import no.nav.tps.forvalteren.common.java.util.YamlPropertySourceFactory;

@Data
@Configuration
@ConfigurationProperties(prefix = "config")
@PropertySource(value = "classpath:tpsprops.yml", factory = YamlPropertySourceFactory.class)
public class TpsPropertiesConfig {

    private static final String DEV_ENVIRONMENT = "D8";
    private static final String PREFIX_MQ_QUEUES = "QA.";
    private static final String MID_PREFIX_QUEUE_ENDRING = "_412.";
    private static final String MID_PREFIX_QUEUE_HENTING = "_411.";
    private static final String ZONE = "FSS";
    private static final String FASIT_APP_NAME = "dummy";
    private static final String QUEUE_MANAGER_ALIAS = "mqGateway";

    private Set<String> environments;
    private List<QueueMgr> queueManagers;

    public QueueManager getQueMgrFromEnv(String env) {
        return queueManagers.stream()
                .filter(entry -> entry.getEnvironment().equals(env.substring(0,1)))
                .map(entry -> no.nav.tps.forvalteren.common.java.util.QueueManager.builder()
                        .name(entry.getQueMgrName())
                        .hostname(entry.getHostname())
                        .port(entry.getPort())
                        .channel(env.toUpperCase() + TpsConstants.CHANNEL_POSTFIX)
                        .build())
                .findFirst().orElse(null);
    }

    public Queue getQueue(String alias, String environment) {

        return Queue.builder()
                .name(new StringBuilder()
                        .append(PREFIX_MQ_QUEUES)
                        .append(environment.toLowerCase().contains("u") ? DEV_ENVIRONMENT : environment.toUpperCase())
                        .append(TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS.equals(alias) ?
                                MID_PREFIX_QUEUE_HENTING : MID_PREFIX_QUEUE_ENDRING)
                        .append(alias)
                        .toString())
                .build();
    }

    @Data
    public static class QueueMgr {

        private String environment;
        private String hostname;
        private String queMgrName;
        private Integer port;
    }
}

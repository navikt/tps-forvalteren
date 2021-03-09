package no.nav.tps.forvalteren.common.tpsapi;

import static no.nav.tps.forvalteren.common.tpsapi.TpsConstants.DEV_ENVIRONMENT;
import static no.nav.tps.forvalteren.common.tpsapi.TpsConstants.MID_PREFIX_QUEUE_ENDRING;
import static no.nav.tps.forvalteren.common.tpsapi.TpsConstants.MID_PREFIX_QUEUE_HENTING;
import static no.nav.tps.forvalteren.common.tpsapi.TpsConstants.PREFIX_MQ_QUEUES;

import java.util.List;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import lombok.Data;
import no.nav.tps.forvalteren.common.util.YamlPropertySourceFactory;

@Data
@Service
@ConfigurationProperties(prefix = "config")
@PropertySource(value = "classpath:tpsprops.yml", factory = YamlPropertySourceFactory.class)
public class TpsPropsService {

    private Set<String> environments;
    private List<QueMgrIntern> queueManagers;

    public QueueManager getQueueManagerByEnv(String env) {
        return queueManagers.stream()
                .filter(entry -> entry.getEnvironment().equals(env.substring(0,1).replace('u','d')))
                .map(entry -> QueueManager.builder()
                        .name(entry.getQueMgrName())
                        .hostname(entry.getHostname())
                        .port(entry.getPort())
                        .channel((env.contains("u") ? DEV_ENVIRONMENT : env.toUpperCase()) + TpsConstants.CHANNEL_POSTFIX)
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
    public static class QueMgrIntern {

        private String environment;
        private String hostname;
        private String queMgrName;
        private Integer port;
    }
}

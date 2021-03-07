package no.nav.tps.forvalteren.consumer.mq.factories;

import static no.nav.tps.forvalteren.common.java.config.CacheConfig.CACHE_TPSCONFIG;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import com.ibm.mq.jms.MQQueueConnectionFactory;

import lombok.extern.slf4j.Slf4j;
import no.nav.tps.forvalteren.common.java.util.QueueManager;

@Slf4j
@Component
public class CachedConnectionFactoryFactory implements ConnectionFactoryFactory {

    @Override
    @Cacheable(value = CACHE_TPSCONFIG, key = "#queueManager.channel")
    public ConnectionFactory createConnectionFactory(QueueManager queueManager) throws JMSException {

        MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
        factory.setTransportType(1);
        factory.setQueueManager(queueManager.getName());
        factory.setHostName(queueManager.getHostname());
        factory.setPort(queueManager.getPort());
        factory.setChannel(queueManager.getChannel());

        if (log.isInfoEnabled()) {
            log.info(String.format("Creating connection factory '%s@%s:%d' on channel '%s' using transport type '%d'",
                    factory.getQueueManager(),
                    factory.getHostName(),
                    factory.getPort(),
                    factory.getChannel(),
                    factory.getTransportType()));
        }

        return factory;
    }
}

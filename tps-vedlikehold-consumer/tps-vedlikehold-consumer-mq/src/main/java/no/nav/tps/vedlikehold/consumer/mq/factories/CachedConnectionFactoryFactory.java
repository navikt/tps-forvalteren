package no.nav.tps.vedlikehold.consumer.mq.factories;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import java.util.concurrent.TimeUnit;


/**
 * A connection factory factory maintaining an internal cache of connection factories.
 * This avoid instantiating new factories for every request, and improves performance.
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Component
public class CachedConnectionFactoryFactory implements QueueManagerConnectionFactoryFactory {

    private static final long CACHE_HOURS_TO_LIVE = 12;

    private Cache<String, ConnectionFactory> cache;


    public CachedConnectionFactoryFactory() {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(CACHE_HOURS_TO_LIVE, TimeUnit.HOURS)
                .build();
    }


    public ConnectionFactory createConnectionFactory(QueueManager queueManager) throws JMSException {
        ConnectionFactory factory = getFactoryForManagerFromCache(queueManager);

        if (factory != null) {
            return factory;
        }

        factory = createConnectionFactoryForManager(queueManager);

        addFactoryForManagerToCache(factory, queueManager);

        return factory;
    }

    /**
     * Create a new connection factory. This is computationally expensive.
     *
     * @param queueManager used to define the manager name, host name, and port
     * @return a new ConnectionFactory
     * @throws JMSException
     */
    private ConnectionFactory createConnectionFactoryForManager(QueueManager queueManager) throws JMSException {
        MQQueueConnectionFactory factory = new MQQueueConnectionFactory();

        Integer transportType   = 1;
        String hostName         = queueManager.getHostname();
        Integer port            = Integer.parseInt(queueManager.getPort());
        String queueManagerName = queueManager.getName();
        String channel          = "T4_SAKOGBEHANDLING";                         //TODO: Make dynamic [PKJAG-2685]

        factory.setTransportType(transportType);
        factory.setQueueManager(queueManagerName);
        factory.setHostName(hostName);
        factory.setPort(port);
        factory.setChannel(channel);

        return factory;
    }

    /* Cache */

    private void addFactoryForManagerToCache(ConnectionFactory factory, QueueManager queueManager) {
        cache.put( getIdentifier(queueManager), factory );
    }

    private ConnectionFactory getFactoryForManagerFromCache(QueueManager queueManager) {
        return cache.getIfPresent( getIdentifier(queueManager) );
    }

    private String getIdentifier(QueueManager queueManager) {
        return String.format("%s.%s.%s", queueManager.getHostname(), queueManager.getPort(), queueManager.getName());
    }
}

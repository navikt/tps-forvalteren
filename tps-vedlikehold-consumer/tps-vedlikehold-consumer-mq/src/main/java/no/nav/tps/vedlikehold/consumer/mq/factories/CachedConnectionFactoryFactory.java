package no.nav.tps.vedlikehold.consumer.mq.factories;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import no.nav.tps.vedlikehold.consumer.mq.factories.strategies.ConnectionFactoryStrategy;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import java.util.concurrent.TimeUnit;


/**
 * A connection factory factory maintaining an internal cache of connection factories.
 * This avoids instantiating new factories for every request, and improves performance.
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Component
public class CachedConnectionFactoryFactory implements ConnectionFactoryFactory {

    private static final long CACHE_HOURS_TO_LIVE = 12;

    private Cache<String, ConnectionFactory> cache;


    public CachedConnectionFactoryFactory() {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(CACHE_HOURS_TO_LIVE, TimeUnit.HOURS)
                .build();
    }


    public ConnectionFactory createConnectionFactory(ConnectionFactoryStrategy strategy) throws JMSException {
        ConnectionFactory factory = getFactoryForManagerFromCache(strategy);

        if (factory != null) {
            return factory;
        }

        factory = createConnectionFactoryForManager(strategy);

        addFactoryForManagerToCache(factory, strategy);

        return factory;
    }

    /**
     * Create a new connection factory. This is computationally expensive.
     *
     * @param strategy used to configure the connection factory
     * @return a new ConnectionFactory
     * @throws JMSException
     */
    private ConnectionFactory createConnectionFactoryForManager(ConnectionFactoryStrategy strategy) throws JMSException {
        MQQueueConnectionFactory factory = new MQQueueConnectionFactory();

        Integer transportType   = strategy.getTransportType();
        String hostName         = strategy.getHostName();
        Integer port            = strategy.getPort();
        String queueManagerName = strategy.getName();
        String channel          = strategy.getChannelName();

        factory.setTransportType(transportType);
        factory.setQueueManager(queueManagerName);
        factory.setHostName(hostName);
        factory.setPort(port);
        factory.setChannel(channel);

        return factory;
    }

    /* Cache */

    private void addFactoryForManagerToCache(ConnectionFactory factory, ConnectionFactoryStrategy strategy) {
        cache.put( getIdentifier(strategy), factory );
    }

    private ConnectionFactory getFactoryForManagerFromCache(ConnectionFactoryStrategy strategy) {
        return cache.getIfPresent( getIdentifier(strategy) );
    }

    private String getIdentifier(ConnectionFactoryStrategy strategy) {
        return String.format("%s.%s.%s", strategy.getHostName(), strategy.getPort(), strategy.getName());
    }
}

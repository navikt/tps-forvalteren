package no.nav.tps.vedlikehold.consumer.mq.factories;

import com.google.common.cache.Cache;
import com.ibm.mq.jms.MQConnectionFactory;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import no.nav.tps.vedlikehold.consumer.mq.factories.strategies.ConnectionFactoryStrategy;
import no.nav.tps.vedlikehold.consumer.mq.factories.strategies.QueueManagerConnectionFactoryStrategy;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;


/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class CachedConnectionFactoryFactoryTest {

    private static final String QUEUE_MANAGER_NAME      = "queueManagerName";
    private static final String QUEUE_MANAGER_HOST_NAME = "queueManagerHostName";
    private static final String QUEUE_MANAGER_PORT      = "1234";

    @Mock
    private Cache<String, ConnectionFactory> cacheMock;

    private ConnectionFactoryStrategy strategy = new QueueManagerConnectionFactoryStrategy(
            new QueueManager(QUEUE_MANAGER_NAME, QUEUE_MANAGER_HOST_NAME, QUEUE_MANAGER_PORT),
            "environment"
    );

    @InjectMocks
    private CachedConnectionFactoryFactory connectionFactoryFactory;

    @Test
    public void connectionFactoryIsRetrievedFromCacheIfPossible() throws JMSException {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);

        when(cacheMock.getIfPresent(anyString())).thenReturn(connectionFactory);

        ConnectionFactory result = connectionFactoryFactory.createConnectionFactory(strategy);

        assertThat(result, equalTo(connectionFactory));

        verify(cacheMock, never()).put(anyString(), eq(connectionFactory));
    }

    @Test
    public void connectionFactoryIsAddedToCacheIfNotPresent() throws JMSException {
        when(cacheMock.getIfPresent(anyString())).thenReturn(null);

        connectionFactoryFactory.createConnectionFactory(strategy);

        verify(cacheMock).put(anyString(), (ConnectionFactory) any());
    }

    @Test
    public void connectionFactoryContainsCorrectProperties() throws JMSException {
        MQConnectionFactory connectionFactory = (MQConnectionFactory) connectionFactoryFactory.createConnectionFactory(strategy);

        assertThat(connectionFactory.getHostName(), is(strategy.getHostName()));
        assertThat(connectionFactory.getQueueManager(), is(strategy.getName()));
        assertThat(connectionFactory.getPort(), is(strategy.getPort()));
        assertThat(connectionFactory.getTransportType(), is(strategy.getTransportType()));
        assertThat(connectionFactory.getChannel(), is(strategy.getChannelName()));
    }
}

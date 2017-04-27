package no.nav.tps.forvalteren.consumer.mq.factories;

import com.google.common.cache.Cache;
import com.ibm.mq.jms.MQConnectionFactory;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.QueueManagerConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;




@RunWith(MockitoJUnitRunner.class)
public class CachedConnectionFactoryFactoryTest {

    private static final String QUEUE_MANAGER_NAME      = "queueManagerName";
    private static final String QUEUE_MANAGER_HOST_NAME = "queueManagerHostName";
    private static final String QUEUE_MANAGER_PORT      = "1234";
    private static final String CHANNEL                 = "CHANNEL";


    @Mock
    private Cache<String, ConnectionFactory> cacheMock;

    private ConnectionFactoryFactoryStrategy strategy = new QueueManagerConnectionFactoryFactoryStrategy(
            new QueueManager(QUEUE_MANAGER_NAME, QUEUE_MANAGER_HOST_NAME, QUEUE_MANAGER_PORT, CHANNEL),
            CHANNEL
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

        verify(cacheMock).put(anyString(), any());
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

package no.nav.tps.vedlikehold.consumer.mq.factories;

import com.google.common.cache.Cache;
import com.ibm.mq.jms.MQConnectionFactory;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;
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
import static org.mockito.Mockito.*;


/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class CachedQueueManagerConnectionFactoryFactoryTest {

    private static final String QUEUE_MANAGER_NAME      = "queueManagerName";
    private static final String QUEUE_MANAGER_HOST_NAME = "queueManagerHostName";
    private static final String QUEUE_MANAGER_PORT      = "1234";

    @Mock
    private Cache<String, ConnectionFactory> cacheMock;

    private QueueManager queueManager = new QueueManager(QUEUE_MANAGER_NAME, QUEUE_MANAGER_HOST_NAME, QUEUE_MANAGER_PORT);

    @InjectMocks
    private CachedConnectionFactoryFactory connectionFactoryFactory;

    @Test
    public void connectionFactoryIsRetrievedFromCacheIfPossible() throws JMSException {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);

        when(cacheMock.getIfPresent(anyString())).thenReturn(connectionFactory);

        ConnectionFactory result = connectionFactoryFactory.createConnectionFactory(queueManager);

        assertThat(result, equalTo(connectionFactory));

        verify(cacheMock, never()).put(anyString(), eq(connectionFactory));
    }

    @Test
    public void connectionFactoryIsAddedToCacheIfNotPresent() throws JMSException {
        when(cacheMock.getIfPresent(anyString())).thenReturn(null);

        connectionFactoryFactory.createConnectionFactory(queueManager);

        verify(cacheMock).put(anyString(), any());
    }

    @Test
    public void connectionFactoryContainsCorrectProperties() throws JMSException {
        MQConnectionFactory connectionFactory = (MQConnectionFactory) connectionFactoryFactory.createConnectionFactory(queueManager);

        assertThat(connectionFactory.getHostName(), is(QUEUE_MANAGER_HOST_NAME));
        assertThat(connectionFactory.getQueueManager(), is(QUEUE_MANAGER_NAME));
        assertThat(connectionFactory.getPort(), is(Integer.parseInt(QUEUE_MANAGER_PORT)));
        assertThat(connectionFactory.getTransportType(), is(1));
        assertThat(connectionFactory.getChannel(), is("T4_SAKOGBEHANDLING"));                   //TODO: Update when this becomes dynamic
    }
}

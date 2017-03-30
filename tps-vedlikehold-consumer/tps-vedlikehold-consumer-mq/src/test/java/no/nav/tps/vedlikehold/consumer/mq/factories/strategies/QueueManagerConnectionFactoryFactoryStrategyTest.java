package no.nav.tps.vedlikehold.consumer.mq.factories.strategies;

import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QueueManagerConnectionFactoryFactoryStrategyTest {

    private static final String QUEUE_MANAGER_NAME            = "queueManagerName";
    private static final String QUEUE_MANAGER_HOST_NAME       = "queueManagerHostName";
    private static final String QUEUE_MANAGER_PORT            = "1234";
    private static final Integer QUEUE_MANAGER_TRANSPORT_TYPE = 1;

    private static final String ENVIRONMENT = "t3";

    @Mock
    private QueueManager queueManagerMock;

    @InjectMocks
    private QueueManagerConnectionFactoryFactoryStrategy connectionFactoryStrategy;

    @Before
    public void setUp() {
        connectionFactoryStrategy = new QueueManagerConnectionFactoryFactoryStrategy(queueManagerMock, ENVIRONMENT);

        when(queueManagerMock.getHostname()).thenReturn(QUEUE_MANAGER_HOST_NAME);
        when(queueManagerMock.getName()).thenReturn(QUEUE_MANAGER_NAME);
        when(queueManagerMock.getPort()).thenReturn(QUEUE_MANAGER_PORT);
    }


    @Test
    public void getsValuesFromTheQueueManager() {
        connectionFactoryStrategy.getName();
        connectionFactoryStrategy.getHostName();
        connectionFactoryStrategy.getPort();

        verify(queueManagerMock).getName();
        verify(queueManagerMock).getHostname();
        verify(queueManagerMock).getPort();
    }

    @Test
    public void returnsCorrectValues() {
        assertThat(connectionFactoryStrategy.getName(), is(QUEUE_MANAGER_NAME));
        assertThat(connectionFactoryStrategy.getPort(), is(Integer.parseInt(QUEUE_MANAGER_PORT)));
        assertThat(connectionFactoryStrategy.getHostName(), is(QUEUE_MANAGER_HOST_NAME));
        assertThat(connectionFactoryStrategy.getTransportType(), is(QUEUE_MANAGER_TRANSPORT_TYPE));
        assertThat(connectionFactoryStrategy.getChannelName(), is(ENVIRONMENT.toUpperCase() + "_TPSWS"));
    }
}

package no.nav.tps.vedlikehold.consumer.mq.factories;

import no.nav.tps.vedlikehold.consumer.mq.factories.strategies.ConnectionFactoryStrategy;
import no.nav.tps.vedlikehold.consumer.ws.fasit.queues.FasitMessageQueueConsumer;
import no.nav.tps.vedlikehold.domain.ws.fasit.Queue;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultMessageQueueConsumerFactoryTest {

    private static final String ENVIRONMENT = "environment";

    @Mock
    FasitMessageQueueConsumer fasitMessageQueueConsumerMock;

    @Mock
    ConnectionFactoryFactory connectionFactoryFactoryMock;

    @InjectMocks
    DefaultMessageQueueServiceFactory serviceFactory;

    @Before
    public void setUp() {
        when(fasitMessageQueueConsumerMock.getRequestQueue(anyString())).thenReturn(mock(Queue.class));
        when(fasitMessageQueueConsumerMock.getResponseQueue(anyString())).thenReturn(mock(Queue.class));
        when(fasitMessageQueueConsumerMock.getQueueManager(anyString(), anyString())).thenReturn(mock(QueueManager.class));
    }

    @Test
    public void retrievesInformationFromFasit() throws JMSException {
        serviceFactory.createMessageQueueService(ENVIRONMENT);

        verify(fasitMessageQueueConsumerMock).getQueueManager(eq(ENVIRONMENT));
        verify(fasitMessageQueueConsumerMock).getRequestQueue(eq(ENVIRONMENT));
        verify(fasitMessageQueueConsumerMock).getResponseQueue(eq(ENVIRONMENT));
    }

    @Test
    public void retrievesAConnectionFactoryFromTheConnectionFactoryFactory() throws JMSException {
        serviceFactory.createMessageQueueService(ENVIRONMENT);

        verify(connectionFactoryFactoryMock).createConnectionFactory(isA(ConnectionFactoryStrategy.class));
    }

}

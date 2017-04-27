package no.nav.tps.forvalteren.consumer.mq.factories;

import no.nav.tps.forvalteren.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.consumer.rs.fasit.queues.FasitMessageQueueConsumer;
import no.nav.tps.forvalteren.domain.ws.fasit.Queue;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;




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
        when(fasitMessageQueueConsumerMock.getQueueManager(anyString(), anyString())).thenReturn(mock(QueueManager.class));
    }

    @Test
    public void retrievesInformationFromFasit() throws JMSException {
        serviceFactory.createMessageQueueConsumer(ENVIRONMENT, REQUEST_QUEUE_SERVICE_RUTINE_ALIAS);

        verify(fasitMessageQueueConsumerMock).getQueueManager(eq(ENVIRONMENT));
        verify(fasitMessageQueueConsumerMock).getRequestQueue(eq(ENVIRONMENT));
    }

    @Test
    public void retrievesAConnectionFactoryFromTheConnectionFactoryFactory() throws JMSException {
        serviceFactory.createMessageQueueConsumer(ENVIRONMENT, REQUEST_QUEUE_SERVICE_RUTINE_ALIAS);

        verify(connectionFactoryFactoryMock).createConnectionFactory(isA(ConnectionFactoryFactoryStrategy.class));
    }

}

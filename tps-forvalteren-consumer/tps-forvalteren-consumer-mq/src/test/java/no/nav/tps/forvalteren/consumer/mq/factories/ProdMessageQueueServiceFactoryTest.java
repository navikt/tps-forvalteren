package no.nav.tps.forvalteren.consumer.mq.factories;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

import javax.jms.JMSException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;

@RunWith(MockitoJUnitRunner.class)
public class ProdMessageQueueServiceFactoryTest {

    private static final String ENVIRONMENT = "P";
    private static final String SERVICE_RUTINE = "FS03-FDNUMMER-KONTINFO-O";

    @Mock
    ConnectionFactoryFactory connectionFactoryFactoryMock;

    @InjectMocks
    ProdMessageQueueServiceFactory serviceFactory;


    @Test
    public void retrievesAConnectionFactoryFromTheConnectionFactoryFactory() throws JMSException {
        serviceFactory.createMessageQueueConsumer(ENVIRONMENT, SERVICE_RUTINE, false);
        verify(connectionFactoryFactoryMock).createConnectionFactory(isA(ConnectionFactoryFactoryStrategy.class));
    }


}

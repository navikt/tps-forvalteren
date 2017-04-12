package no.nav.tps.vedlikehold.consumer.mq.factories;

import no.nav.tps.vedlikehold.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

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
        serviceFactory.createMessageQueueService(ENVIRONMENT, SERVICE_RUTINE);
        verify(connectionFactoryFactoryMock).createConnectionFactory(isA(ConnectionFactoryFactoryStrategy.class));
    }


}

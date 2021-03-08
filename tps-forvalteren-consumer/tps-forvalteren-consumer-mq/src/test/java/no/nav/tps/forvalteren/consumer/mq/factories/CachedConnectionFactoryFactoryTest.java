package no.nav.tps.forvalteren.consumer.mq.factories;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.jms.JMSException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import com.ibm.mq.jms.MQConnectionFactory;

import no.nav.tps.forvalteren.common.tpsapi.QueueManager;

@RunWith(MockitoJUnitRunner.class)
public class CachedConnectionFactoryFactoryTest {

    private static final String QUEUE_MANAGER_NAME      = "queueManagerName";
    private static final String QUEUE_MANAGER_HOST_NAME = "queueManagerHostName";
    private static final Integer QUEUE_MANAGER_PORT      = 1234;
    private static final String CHANNEL                 = "CHANNEL";

    private QueueManager queueManager =
            new QueueManager(QUEUE_MANAGER_NAME, QUEUE_MANAGER_HOST_NAME, QUEUE_MANAGER_PORT, CHANNEL);

    @InjectMocks
    private CachedConnectionFactoryFactory connectionFactoryFactory;

    @Test
    public void connectionFactoryContainsCorrectProperties() throws JMSException {
        MQConnectionFactory connectionFactory = (MQConnectionFactory) connectionFactoryFactory.createConnectionFactory(queueManager);

        assertThat(connectionFactory.getHostName(), is(queueManager.getHostname()));
        assertThat(connectionFactory.getQueueManager(), is(queueManager.getName()));
        assertThat(connectionFactory.getPort(), is(queueManager.getPort()));
        assertThat(connectionFactory.getTransportType(), is(1));
        assertThat(connectionFactory.getChannel(), is(queueManager.getChannel()));
    }
}

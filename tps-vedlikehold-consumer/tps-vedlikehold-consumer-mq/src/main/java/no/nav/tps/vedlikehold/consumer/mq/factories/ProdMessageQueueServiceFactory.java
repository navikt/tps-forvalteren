package no.nav.tps.vedlikehold.consumer.mq.factories;

import no.nav.tps.vedlikehold.consumer.mq.consumers.DefaultMessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;
import no.nav.tps.vedlikehold.consumer.mq.factories.strategies.QueueManagerConnectionFactoryFactoryStrategy;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

@Profile("prod")
@Component
public class ProdMessageQueueServiceFactory implements MessageQueueServiceFactory {

    public static final String REQUEST_QUEUE_ALIAS = "QA.D8_411.TPS_FORESPORSEL_XML_O";

    public static final String QUEUE_MANAGER_ALIAS = "MDLCLIENT03";
    public static final String HOSTNAME = "e26apvl100.test.local";
    public static final String PORT = "1411";
    public static final String CHANNEL_NAME = "U1_TPS_VEDLIKEHOLD";

    @Autowired
    private ConnectionFactoryFactory connectionFactoryFactory;

    @Override
    public MessageQueueConsumer createMessageQueueService(String environment, String requestQueueAlias) throws JMSException {

        QueueManager queueManager = new QueueManager(QUEUE_MANAGER_ALIAS, HOSTNAME, PORT, CHANNEL_NAME);

        ConnectionFactoryFactoryStrategy connectionFactoryFactoryStrategy = new QueueManagerConnectionFactoryFactoryStrategy(queueManager, CHANNEL_NAME);

        ConnectionFactory connectionFactory = connectionFactoryFactory.createConnectionFactory(connectionFactoryFactoryStrategy);

        return new DefaultMessageQueueConsumer(
                REQUEST_QUEUE_ALIAS,
                connectionFactory
        );
    }

}

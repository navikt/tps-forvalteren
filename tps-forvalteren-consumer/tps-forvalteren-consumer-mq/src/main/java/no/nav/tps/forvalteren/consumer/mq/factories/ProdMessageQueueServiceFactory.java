package no.nav.tps.forvalteren.consumer.mq.factories;

import static no.nav.tps.forvalteren.consumer.mq.config.MessageQueueConsumerConstants.CHANNEL_POSTFIX;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.QueueManagerConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;

@Component
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "true")
public class ProdMessageQueueServiceFactory implements MessageQueueServiceFactory {

    @Value("${TPS_FORESPORSEL_XML_O_QUEUENAME}")
    private String tpsRequestQueue;

    @Value("${MQGATEWAY_NAME}")
    private String queueManagerAlias;

    @Value("${MQGATEWAY_HOSTNAME}")
    private String hostname;

    @Value("${MQGATEWAY_PORT}")
    private String port;


    @Autowired
    private ConnectionFactoryFactory connectionFactoryFactory;

    @Override
    public MessageQueueConsumer createMessageQueueConsumer(String environment, String requestQueueAlias) throws JMSException {
        QueueManager queueManager = new QueueManager(queueManagerAlias, hostname, port, (environment).toUpperCase() + CHANNEL_POSTFIX);

        ConnectionFactoryFactoryStrategy connectionFactoryFactoryStrategy = new QueueManagerConnectionFactoryFactoryStrategy(queueManager,
                (environment).toUpperCase() + CHANNEL_POSTFIX);

        ConnectionFactory connectionFactory = connectionFactoryFactory.createConnectionFactory(connectionFactoryFactoryStrategy);

        return new MessageQueueConsumer(
                tpsRequestQueue,
                connectionFactory
        );
    }

}

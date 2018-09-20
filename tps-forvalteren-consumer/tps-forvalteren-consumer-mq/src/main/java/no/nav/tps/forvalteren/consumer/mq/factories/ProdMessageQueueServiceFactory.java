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

    @Value("${tps.foresporsel.xml.o.queuename}")
    private String tpsRequestQueue;

    @Value("${mqgateway.name}")
    private String queueManagerAlias;

    @Value("${mqgateway.hostname}")
    private String hostname;

    @Value("${mqgateway.port}")
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

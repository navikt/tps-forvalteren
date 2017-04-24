package no.nav.tps.forvalteren.consumer.mq.factories;

import no.nav.tps.forvalteren.consumer.mq.consumers.DefaultMessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.QueueManagerConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

@Component
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "true")
public class ProdMessageQueueServiceFactory implements MessageQueueServiceFactory {

    @Value("${TPS_FORESPORSEL_XML_O.queueName}")
    private String tpsRequestQueue;

    @Value("${mqGateway.name}")
    private String queueManagerAlias;

    @Value("${mqGateway.hostname}")
    private String hostname;

    @Value("${mqGateway.port}")
    private String port;

    @Value("${mqGateway.channel}")
    private String channelName;

    @Autowired
    private ConnectionFactoryFactory connectionFactoryFactory;

    @Override
    public MessageQueueConsumer createMessageQueueConsumer(String environment, String requestQueueAlias) throws JMSException {
        QueueManager queueManager = new QueueManager(queueManagerAlias, hostname, port, channelName);

        ConnectionFactoryFactoryStrategy connectionFactoryFactoryStrategy = new QueueManagerConnectionFactoryFactoryStrategy(queueManager, channelName);

        ConnectionFactory connectionFactory = connectionFactoryFactory.createConnectionFactory(connectionFactoryFactoryStrategy);

        return new DefaultMessageQueueConsumer(
                tpsRequestQueue,
                connectionFactory
        );
    }

}

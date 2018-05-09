package no.nav.tps.forvalteren.consumer.mq.factories;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import static no.nav.tps.forvalteren.consumer.mq.config.MessageQueueConsumerConstants.CHANNEL_POSTFIX;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.QueueManagerConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.consumer.rs.fasit.queues.FasitMessageQueueConsumer;
import no.nav.tps.forvalteren.domain.ws.fasit.Queue;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Consumes information from Fasit and produces MessageQueueServices
 */
@Component
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false", matchIfMissing = true)
public class DefaultMessageQueueServiceFactory implements MessageQueueServiceFactory {

    @Autowired
    private FasitMessageQueueConsumer fasitMessageQueueConsumer;

    @Autowired
    private ConnectionFactoryFactory connectionFactoryFactory;

    /**
     * Instantiates a new MessageQueueConsumer in the specified environment
     *
     * @param environment in which the messages will be exchanged
     * @return A MessageQueueConsumer providing communication with an MQ in the specified environment
     * @throws JMSException
     */
    @Override
    public MessageQueueConsumer createMessageQueueConsumer(String environment, String requestQueueAlias) throws JMSException {

        QueueManager queueManager = fasitMessageQueueConsumer.getQueueManager(environment);
        Queue requestQueue        = fasitMessageQueueConsumer.getRequestQueue(requestQueueAlias, environment);

        ConnectionFactoryFactoryStrategy connectionFactoryFactoryStrategy = new QueueManagerConnectionFactoryFactoryStrategy(queueManager,
                                                                    (environment).toUpperCase() + CHANNEL_POSTFIX);

        ConnectionFactory connectionFactory = connectionFactoryFactory.createConnectionFactory(connectionFactoryFactoryStrategy);

        return new MessageQueueConsumer(
                requestQueue.getName(),
                connectionFactory
        );
    }

}

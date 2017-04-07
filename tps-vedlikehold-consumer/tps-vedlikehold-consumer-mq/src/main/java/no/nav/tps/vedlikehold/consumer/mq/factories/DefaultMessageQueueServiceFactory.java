package no.nav.tps.vedlikehold.consumer.mq.factories;

import no.nav.tps.vedlikehold.consumer.mq.consumers.DefaultMessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;
import no.nav.tps.vedlikehold.consumer.mq.factories.strategies.QueueManagerConnectionFactoryFactoryStrategy;
import no.nav.tps.vedlikehold.consumer.rs.fasit.queues.FasitMessageQueueConsumer;
import no.nav.tps.vedlikehold.domain.ws.fasit.Queue;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import static no.nav.tps.vedlikehold.consumer.mq.config.MessageQueueConsumerConstants.CHANNEL_POSTFIX;

/**
 * Consumes information from Fasit and produces MessageQueueServices
 */

@Profile("default")
@Component
public class DefaultMessageQueueServiceFactory implements MessageQueueServiceFactory {

    @Autowired
    private FasitMessageQueueConsumer fasitMessageQueueConsumer;

    // TODO: There should not be any dependencies between consumers
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
    public DefaultMessageQueueConsumer createMessageQueueService(String environment, String requestQueueAlias) throws JMSException {

        fasitMessageQueueConsumer.setRequestQueueAlias(requestQueueAlias);
        QueueManager queueManager = fasitMessageQueueConsumer.getQueueManager(environment);
        Queue requestQueue        = fasitMessageQueueConsumer.getRequestQueue(environment);

        ConnectionFactoryFactoryStrategy connectionFactoryFactoryStrategy = new QueueManagerConnectionFactoryFactoryStrategy(queueManager, environment, environment.toUpperCase() + CHANNEL_POSTFIX);

        ConnectionFactory connectionFactory = connectionFactoryFactory.createConnectionFactory(connectionFactoryFactoryStrategy);

        return new DefaultMessageQueueConsumer(
                requestQueue.getName(),
                connectionFactory
        );
    }

}

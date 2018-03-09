package no.nav.tps.forvalteren.consumer.mq.factories;

import no.nav.tps.forvalteren.consumer.mq.consumers.DefaultMessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.QueueManagerConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.consumer.rs.fasit.queues.FasitMessageQueueConsumer;
import no.nav.tps.forvalteren.domain.ws.fasit.Queue;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${FASIT_ENVIRONMENT_NAME}")
    private String deployedEnvironment;

    /**
     * Instantiates a new MessageQueueConsumer in the specified environment
     *
     * @param environment in which the messages will be exchanged
     * @return A MessageQueueConsumer providing communication with an MQ in the specified environment
     * @throws JMSException
     */
    @Override
    public DefaultMessageQueueConsumer createMessageQueueConsumer(String environment, String requestQueueAlias) throws JMSException {

        QueueManager queueManager = fasitMessageQueueConsumer.getQueueManager();
        Queue requestQueue        = fasitMessageQueueConsumer.getRequestQueue(requestQueueAlias, environment);

        ConnectionFactoryFactoryStrategy connectionFactoryFactoryStrategy = new QueueManagerConnectionFactoryFactoryStrategy(queueManager, "T0_PERSON_TPS_MOTTAK");

        ConnectionFactory connectionFactory = connectionFactoryFactory.createConnectionFactory(connectionFactoryFactoryStrategy);

        return new DefaultMessageQueueConsumer(
                requestQueue.getName(),
                connectionFactory
        );
    }

}

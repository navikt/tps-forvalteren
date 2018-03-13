package no.nav.tps.forvalteren.consumer.mq.factories;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import static no.nav.tps.forvalteren.consumer.mq.config.MessageQueueConsumerConstants.CHANNEL_POSTFIX;
import no.nav.tps.forvalteren.consumer.mq.consumers.DefaultMessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.QueueManagerConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.consumer.rs.fasit.queues.FasitMessageQueueConsumer;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Consumes information from Fasit and produces MessageQueueServices
 */
@Component
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false", matchIfMissing = true)
public class DefaultMessageFixedQueueServiceFactory implements MessageFixedQueueServiceFactory {

    private static final String DEFAULT_ENVIRONMENT_NUMBER = "6";

    @Autowired
    private FasitMessageQueueConsumer fasitMessageQueueConsumer;

    @Autowired
    private ConnectionFactoryFactory connectionFactoryFactory;

    @Value("${environment.class}")
    private String deployedEnvironment;

    /**
     * Instantiates a new MessageQueueConsumer in the specified environment
     *
     * @param environment in which the messages will be exchanged
     * @return A MessageQueueConsumer providing communication with an MQ in the specified environment
     * @throws JMSException
     */
    @Override
    public DefaultMessageQueueConsumer createMessageQueueConsumerWithFixedQueueName(String environment, String fixedQueueName) throws JMSException {

        QueueManager queueManager = fasitMessageQueueConsumer.getQueueManager(environment);

        ConnectionFactoryFactoryStrategy connectionFactoryFactoryStrategy = new QueueManagerConnectionFactoryFactoryStrategy(queueManager,
                (deployedEnvironment + DEFAULT_ENVIRONMENT_NUMBER).toUpperCase() + CHANNEL_POSTFIX);

        ConnectionFactory connectionFactory = connectionFactoryFactory.createConnectionFactory(connectionFactoryFactoryStrategy);

        return new DefaultMessageQueueConsumer(
                fixedQueueName,
                connectionFactory);
    }

}

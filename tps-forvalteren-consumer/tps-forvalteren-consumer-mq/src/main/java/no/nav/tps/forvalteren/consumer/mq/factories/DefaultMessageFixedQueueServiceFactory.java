package no.nav.tps.forvalteren.consumer.mq.factories;

import javax.jms.JMSException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.common.java.tpsapi.QueueManager;
import no.nav.tps.forvalteren.common.java.tpsapi.TpsPropsService;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;

/**
 * Consumes information from Fasit and produces MessageQueueServices
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false", matchIfMissing = true)
public class DefaultMessageFixedQueueServiceFactory implements MessageFixedQueueServiceFactory {

    private final TpsPropsService tpsProperties;
    private final ConnectionFactoryFactory connectionFactoryFactory;

    /**
     * Instantiates a new MessageQueueConsumer in the specified environment
     *
     * @param environment in which the messages will be exchanged
     * @return A MessageQueueConsumer providing communication with an MQ in the specified environment
     * @throws JMSException
     */
    @Override
    public MessageQueueConsumer createMessageQueueConsumerWithFixedQueueName(String environment, String fixedQueueName) throws JMSException {

        environment = environment.toLowerCase();
        if (environment.contains("d")) {
            environment = "u6";
        }

        QueueManager queueManager = tpsProperties.getQueueManagerByEnv(environment);

        return MessageQueueConsumer.builder()
                .requestQueueName(fixedQueueName.toUpperCase())
                .connectionFactory(connectionFactoryFactory.createConnectionFactory(queueManager))
                .build();
    }
}

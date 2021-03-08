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
public class DefaultMessageQueueServiceFactory implements MessageQueueServiceFactory {

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
       public MessageQueueConsumer createMessageQueueConsumer(String environment, String requestQueueAlias, boolean isQueName) throws JMSException {

        QueueManager queueManager = tpsProperties.getQueueManagerByEnv(environment);

        return MessageQueueConsumer.builder()
                .connectionFactory(connectionFactoryFactory.createConnectionFactory(queueManager))
                .requestQueueName(isQueName ? requestQueueAlias :
                        tpsProperties.getQueue(requestQueueAlias,environment).getName())
                .build();
    }
}

package no.nav.tps.forvalteren.consumer.mq.factories;

import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.common.tpsapi.QueueManager;
import no.nav.tps.forvalteren.common.tpsapi.TpsConstants;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "true")
public class ProdMessageQueueServiceFactory implements MessageQueueServiceFactory {

    @Value("${tps.foresporsel.xml.o.queuename}")
    private String tpsRequestQueue;

    @Value("${mqgateway.name}")
    private String queueManagerAlias;

    @Value("${mqgateway.hostname}")
    private String hostname;

    @Value("${mqgateway.port}")
    private Integer port;

    private final ConnectionFactoryFactory connectionFactoryFactory;

    @Override
    public MessageQueueConsumer createMessageQueueConsumer(String environment, String requestQueueAlias, boolean isQueueName) throws JMSException {
        QueueManager queueManager = new QueueManager(queueManagerAlias, hostname, port, (environment).toUpperCase() + TpsConstants.CHANNEL_POSTFIX);

        return MessageQueueConsumer.builder()
                .requestQueueName(tpsRequestQueue)
                .connectionFactory(connectionFactoryFactory.createConnectionFactory(queueManager))
                .build();
    }
}

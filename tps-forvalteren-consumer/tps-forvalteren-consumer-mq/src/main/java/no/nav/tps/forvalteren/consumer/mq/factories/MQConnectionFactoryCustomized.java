package no.nav.tps.forvalteren.consumer.mq.factories;

import javax.jms.JMSException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.common.tpsapi.QueueManager;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;

@Component
@RequiredArgsConstructor
public class MQConnectionFactoryCustomized {

    private final ConnectionFactoryFactory connectionFactoryFactory;

    public MessageQueueConsumer createMessageQueueConsumer(String manager, String host, Integer port, String queue, String channel) throws JMSException {

        QueueManager qm = QueueManager.builder()
                .name(manager)
                .hostname(host)
                .port(port)
                .channel(channel)
                .build();

        return MessageQueueConsumer.builder()
                .requestQueueName(queue)
                .connectionFactory(connectionFactoryFactory.createConnectionFactory(qm))
                .build();
    }
}

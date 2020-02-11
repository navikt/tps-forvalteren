package no.nav.tps.forvalteren.consumer.mq.factories;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.ConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.consumer.mq.factories.strategies.QueueManagerConnectionFactoryFactoryStrategy;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;

@Component
public class MQConnectionFactoryByFasit {

    @Autowired
    private ConnectionFactoryFactory connectionFactoryFactory;

    public MessageQueueConsumer createMessageQueueConsumer(String manager, String host, String port, String queue, String channel) throws JMSException {

        //TODO dette skal gjøres om etter hvert. Channel er waste at den ikke abre er i QMaanger
        QueueManager qm = new QueueManager(manager, host, port, channel);
        ConnectionFactoryFactoryStrategy connectionFactoryFactoryStrategy = new QueueManagerConnectionFactoryFactoryStrategy(qm, channel);

        ConnectionFactory connectionFactory = connectionFactoryFactory.createConnectionFactory(connectionFactoryFactoryStrategy);

        return new MessageQueueConsumer(queue, connectionFactory);
    }
}

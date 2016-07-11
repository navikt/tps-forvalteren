package no.nav.tps.vedlikehold.consumer.mq;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import no.nav.tps.vedlikehold.consumer.ws.fasit.queue.FasitMessageQueueConsumer;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class MessageQueueConnectionFactoryFactory {

    static ConnectionFactory connectionFactory(QueueManager queueManager) throws JMSException {
        MQQueueConnectionFactory factory = new MQQueueConnectionFactory();

        Integer transportType   = 1;
        String hostName         = "d26apvl126.test.local"; //queueManager.getHostname();
        Integer port            = 1412; //Integer.parseInt(queueManager.getPort());
        String queueManagerName = "MTLCLIENT01"; //queueManager.getName();
        String channel          = "T4_SAKOGBEHANDLING"; //"TPSWS." + queueManager.getName();

        factory.setTransportType(transportType);
        factory.setQueueManager(queueManagerName);
        factory.setHostName(hostName);
        factory.setPort(port);
        factory.setChannel(channel);

        return factory;
    }

}

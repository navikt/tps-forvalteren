package no.nav.tps.vedlikehold.consumer.mq;


import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.v6.base.internal.MQC;
import no.nav.tps.vedlikehold.consumer.ws.fasit.FasitClient;
import no.nav.tps.vedlikehold.consumer.ws.fasit.FasitConfig;
import no.nav.tps.vedlikehold.consumer.ws.fasit.queue.FasitMessageQueueConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;

import javax.annotation.PostConstruct;
import javax.jms.*;
import java.util.Properties;
import com.ibm.mq.jmqi.JmqiException;


/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@SpringBootApplication
@EnableJms
@Import({
        FasitConfig.class
})
public class MessageQueueConfig {

    @Autowired
    ConfigurableApplicationContext context;

    @Autowired
    MessageQueueSender messageSender;

    @Autowired
    FasitMessageQueueConsumer fasitMessageQueueConsumer;

//    @Bean
//    public JmsTemplate jmsTemplate() {
//        JmsTemplate template = new JmsTemplate(connectionFactory());
//
//        return template;
//    }

//    @Bean
//    public ConnectionFactory connectionFactory() {
//
//        FasitClient.QueueManager queueManager = fasitMessageQueueConsumer.getQueueManager("mqGateway", "u5");
//        String host = "tcp://" + queueManager.getHostname() + ":" + queueManager.getPort() + "?wireFormat.maxInactivityDuration=30000";
//
//        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(host);
//
//        // Noe mer her? Channel-en feiler
//
//        return factory;
//    }

    public static void main(String args[]) {
        SpringApplication.run(MessageQueueConfig.class);
    }

    @PostConstruct
    public void run() {
        try {
            String queueName = fasitMessageQueueConsumer.getRequestQueue("u5").getName();

            FasitClient.QueueManager queueManager = fasitMessageQueueConsumer.getQueueManager("mqGateway", "u5");

            MQQueueConnectionFactory factory = new MQQueueConnectionFactory();

            Integer transportType   = 1;
            String hostName         = queueManager.getHostname();
            Integer port            = Integer.parseInt(queueManager.getPort());
            String queueManagerName = queueManager.getName();
//            String channel          = "TPSWS." + queueManager.getName();

            factory.setStringProperty(MQC.PASSWORD_PROPERTY, "admin");
            factory.setStringProperty(MQC.USER_ID_PROPERTY, "admin");

            factory.setTransportType(transportType);
            factory.setQueueManager(queueManagerName);
            factory.setHostName(hostName);
            factory.setPort(port);
//            factory.setChannel(channel);

            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue(fasitMessageQueueConsumer.getRequestQueue("u1").getName());

            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            String text = "If this causes problems, contact me at oyvind.grimnes@nav.no";
            TextMessage message = session.createTextMessage(text);

            producer.send(message);

            session.close();
            connection.close();
        } catch (JMSException exception) {
            System.err.println("\n" + exception.getMessage());
            exception.printStackTrace();
            System.err.println("\n");
        }

//        new Timer().schedule(
//                new TimerTask() {
//                    @Override
//                    public void run() {
//                        messageSender.sendMessage("Kommer denne frem?", "u6");
//                    }
//                }, 5000
//        );
    }
}

package no.nav.tps.vedlikehold.consumer.mq;


import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.jms.internal.JMSComponent;
import com.ibm.msg.client.wmq.v6.base.internal.MQC;
import no.nav.tps.vedlikehold.consumer.ws.fasit.FasitClient;
import no.nav.tps.vedlikehold.consumer.ws.fasit.FasitConfig;
import no.nav.tps.vedlikehold.consumer.ws.fasit.queue.FasitMessageQueueConsumer;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;

import javax.annotation.PostConstruct;
import javax.jms.*;
import java.util.Properties;
import com.ibm.mq.jmqi.JmqiException;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;


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

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate(connectionFactory());

        return template;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        QueueManager manager = fasitMessageQueueConsumer.getQueueManager("mqGateway", "u1");

        try {
            return MessageQueueConnectionFactoryFactory.connectionFactory(manager);
        } catch (JMSException exception) {
            System.err.println("\n" + exception.getMessage());
            exception.printStackTrace();
            System.err.println("\n");
        }

        return null;
    }

    public static void main(String args[]) {
        SpringApplication.run(MessageQueueConfig.class);
    }

    @PostConstruct
    public void run() {
        try {
            ConnectionFactory factory = connectionFactory();

            Connection connection = factory.createConnection("srvappserver", "");
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue("QA.T4_HEND.SAKSBEHANDLING");

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
    }
}

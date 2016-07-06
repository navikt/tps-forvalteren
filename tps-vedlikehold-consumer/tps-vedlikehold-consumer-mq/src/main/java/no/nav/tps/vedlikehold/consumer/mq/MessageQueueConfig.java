package no.nav.tps.vedlikehold.consumer.mq;

import no.nav.tps.vedlikehold.consumer.ws.fasit.FasitConfig;
import no.nav.tps.vedlikehold.consumer.ws.fasit.queue.FasitMessageQueueConsumer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import java.util.Timer;
import java.util.TimerTask;

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
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://10.33.14.76:1411");

        // Noe mer her? Channel-en feiler

        return factory;
    }

    public static void main(String args[]) {
        SpringApplication.run(MessageQueueConfig.class);
    }

    @PostConstruct
    public void run() {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        messageSender.sendMessage("Kommer denne frem?");
                    }
                }, 5000
        );
    }
}

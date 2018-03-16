package no.nav.tps.forvalteren.provider.rs.api.v1.config;

import no.nav.tjeneste.pip.diskresjonskode.binding.DiskresjonskodePortType;
import no.nav.tps.forvalteren.consumer.mq.consumers.DefaultMessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.forvalteren.provider.config.IntegrationTestConfig;
import no.nav.tps.forvalteren.provider.rs.config.RestProviderConfig;

import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import java.io.IOException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackageClasses = TestUserDetails.class)
@EnableJms
@Import({
        IntegrationTestConfig.class,
        RestProviderConfig.class
})
public class RsProviderIntegrationTestConfig {

    public static final String TPS_TEST_REQUEST_QUEUE = "tps.test.request.queue";
    public static final String TPS_TEST_RESPONSE_QUEUE = "tps.test.response.queue";

    @Bean
    @Primary
    public DiskresjonskodeConsumer diskresjonskodeConsumerMock() {
        return mock(DiskresjonskodeConsumer.class);
    }

    @Bean
    @Primary
    public DiskresjonskodePortType diskresjonskodePortType() {
        return mock(DiskresjonskodePortType.class);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new TestUserDetails();
    }

    @Bean
    @Primary
    public EgenAnsattConsumer egenAnsattConsumer() {
        return mock(EgenAnsattConsumer.class);
    }

    @Bean
    @Primary
    public MessageQueueServiceFactory defaultMessageQueueServiceFactory() {
        return (environment, requestQueueAlias) -> defaultMessageQueueConsumer();
    }

    @Bean
    @Primary
    public MessageQueueConsumer defaultMessageQueueConsumer() {
        return Mockito.spy(new DefaultMessageQueueConsumer(TPS_TEST_REQUEST_QUEUE, connectionFactory()));
    }

    @Bean
    public QueueConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false&broker.useShutdownHook=false");
    }

    @Bean
    public Queue requestQueue() {
        return new ActiveMQQueue(TPS_TEST_REQUEST_QUEUE);
    }

    @Bean
    public Queue responseQueue() {
        return new ActiveMQQueue(TPS_TEST_RESPONSE_QUEUE);
    }

    @Bean
    public JmsTemplate jmsTemplate() throws IOException {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setReceiveTimeout(100);
        return jmsTemplate;
    }

}

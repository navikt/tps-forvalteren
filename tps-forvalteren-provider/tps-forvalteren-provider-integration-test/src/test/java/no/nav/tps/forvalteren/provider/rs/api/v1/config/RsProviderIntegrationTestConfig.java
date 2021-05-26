package no.nav.tps.forvalteren.provider.rs.api.v1.config;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
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

import no.nav.tjeneste.pip.diskresjonskode.DiskresjonskodePortType;
import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.common.tpsapi.TpsPropsService;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.consumer.rs.adresser.AdresseServiceConsumer;
import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;
import no.nav.tps.forvalteren.consumer.rs.identpool.IdentpoolConsumer;
import no.nav.tps.forvalteren.consumer.rs.kodeverk.KodeverkConsumer;
import no.nav.tps.forvalteren.consumer.ws.sts.TpsfStsClient;
import no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.forvalteren.provider.config.IntegrationTestConfig;
import no.nav.tps.forvalteren.provider.rs.config.RestProviderConfig;
import no.nav.tps.forvalteren.service.IdentpoolService;

@Configuration
@ComponentScan(basePackages = "no.nav.tps.forvalteren.provider")
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
    public DiskresjonskodePortType diskresjonskodePortType() {
        return mock(DiskresjonskodePortType.class);
    }

    @Bean
    @Primary
    public DiskresjonskodeConsumer diskresjonskodeConsumerMock() {
        return mock(DiskresjonskodeConsumer.class);
    }

    @Bean
    @Primary
    public EgenAnsattConsumer egenAnsattConsumer() {
        return mock(EgenAnsattConsumer.class);
    }

    /*
    Legger til TpsfSts klient som mocker og @Primary, slik at Spring prioriterer Mockene når de finner flere bønner --
    -- av type TpsfStsClient. Hvis man ikke mocker, men mocker consumer, så får man en feil om at input proxy type ikker er riktig bla bla...
     */
    @Bean(name = "cxfStsClientDiskresjonskode")
    @Primary
    public TpsfStsClient cxfStsClientDiskresjonskode() {
        return mock(TpsfStsClient.class);
    }

    @Bean(name = "cxfStsClientEgenAnsatt")
    @Primary
    public TpsfStsClient cxfStsClientEgenAnsatt() {
        return mock(TpsfStsClient.class);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new TestUserDetails();
    }

    @Bean
    @Primary
    public MessageQueueServiceFactory defaultMessageQueueServiceFactory() {
        return (environment, requestQueueAlias, isQueName) -> defaultMessageQueueConsumer();
    }

    @Bean
    @Primary
    public MessageQueueConsumer defaultMessageQueueConsumer() {
        return Mockito.spy(MessageQueueConsumer.builder()
                .requestQueueName(TPS_TEST_REQUEST_QUEUE)
                .connectionFactory(connectionFactory())
                .build());
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

    @Bean
    public MessageProvider messageProvider() {
        return new MessageProvider();
    }

    @Bean
    public IdentpoolService identpoolService() {
        return mock(IdentpoolService.class);
    }

    @Bean
    public IdentpoolConsumer identpoolConsumer() {
        return mock(IdentpoolConsumer.class);
    }

    @Bean
    public KodeverkConsumer kodeverkConsumer() {
        return mock(KodeverkConsumer.class);
    }

    @Bean
    public FasitApiConsumer fasitApiConsumer() {
        return mock(FasitApiConsumer.class);
    }

    @Bean
    public TpsPropsService tpsProperties() {
        return mock(TpsPropsService.class);
    }

    @Bean
    public AdresseServiceConsumer adresseServiceConsumer() {
        return mock(AdresseServiceConsumer.class);
    }
}

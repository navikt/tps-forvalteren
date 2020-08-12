package no.nav.tps.forvalteren;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetailsService;

import no.nav.tps.forvalteren.config.TestLandkodeEncoder;
import no.nav.tps.forvalteren.config.TestUserDetails;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;
import no.nav.tps.forvalteren.consumer.rs.environments.FetchEnvironmentsManager;
import no.nav.tps.forvalteren.consumer.rs.fasit.FasitClient;
import no.nav.tps.forvalteren.consumer.ws.sts.TpsfStsClient;
import no.nav.tps.forvalteren.service.command.testdata.FiktiveIdenterGenerator;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

@Configuration
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = FetchEnvironmentsManager.class)
})
public class ComptestConfig {

    public static final String TPS_TEST_REQUEST_QUEUE = "tps.test.request.queue";
    public static final String TPS_TEST_RESPONSE_QUEUE = "tps.test.response.queue";
    public static List<Pair<String, String>> actualConnectedToEnvironments = new ArrayList<>();

    @Bean
    @Primary
    public FetchEnvironmentsManager fetchEnvironmentsManagerSpy(FetchEnvironmentsManager fetchEnvironmentsManager) {
        return Mockito.spy(fetchEnvironmentsManager);
    }

    @Bean
    @Primary
    public FiktiveIdenterGenerator fiktiveIdenterGenerator() {
        return mock(FiktiveIdenterGenerator.class);
    }

    @Bean
    @Primary
    public FasitClient fasitClient() {
        return mock(FasitClient.class);
    }

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
        return (environment, requestQueueAlias, isQueueName) -> {
            actualConnectedToEnvironments.add(Pair.of(environment, requestQueueAlias));
            return defaultMessageQueueConsumer();
        };
    }

    @Bean
    @Primary
    public MessageQueueConsumer defaultMessageQueueConsumer() {
        return Mockito.mock(MessageQueueConsumer.class);
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
    public LandkodeEncoder landkodeEncoder() {
        return new TestLandkodeEncoder();
    }

    @Bean
    public FasitApiConsumer fasitApiConsumer() {
        return mock(FasitApiConsumer.class);
    }

    @Bean
    public ForkJoinPool tpsfForkJoinPool() {
        return new ForkJoinPool(10, new ForkJoinWorkerThreadFactory(), null, true);
    }
}

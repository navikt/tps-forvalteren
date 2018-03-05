package no.nav.tps.forvalteren.consumer.rs.fasit.config;

import no.nav.tps.forvalteren.consumer.rs.fasit.FasitClient;
import no.nav.tps.forvalteren.consumer.rs.fasit.queues.DefaultFasitMessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.rs.fasit.queues.FasitMessageQueueConsumer;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



@Configuration
@ComponentScan(basePackageClasses = {
        FasitClient.class
})
public class FasitConfig {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

//    @Bean
//    public FasitMessageQueueConsumer getTpswsFasitMessageQueueQueueConsumer() {
//        FasitMessageQueueConsumer consumer = new DefaultFasitMessageQueueConsumer(
//                FasitConstants.FASIT_APPLICATION_NAME,
//                TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS,
//                FasitConstants.QUEUE_MANAGER_ALIAS
//        );
//
//        /* Inject a FasitClient object */
//        beanFactory.autowireBean(consumer);
//        return consumer;
//    }

    @Bean
    public FasitClient getFasitClient() {
        return new FasitClient(
                FasitConstants.BASE_URL,
                FasitConstants.USERNAME,
                FasitConstants.PASSWORD
        );
    }
}

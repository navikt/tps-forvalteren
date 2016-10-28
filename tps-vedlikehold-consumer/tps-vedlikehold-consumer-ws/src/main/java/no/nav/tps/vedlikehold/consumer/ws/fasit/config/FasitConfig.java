package no.nav.tps.vedlikehold.consumer.ws.fasit.config;

import static no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConstants.APPLICATION_NAME;
import static no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConstants.BASE_URL;
import static no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConstants.PASSWORD;
import static no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConstants.QUEUE_MANAGER_ALIAS;
import static no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConstants.RESPONSE_QUEUE_ALIAS;
import static no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConstants.USERNAME;

import no.nav.tps.vedlikehold.consumer.ws.fasit.FasitClient;
import no.nav.tps.vedlikehold.consumer.ws.fasit.queues.DefaultFasitMessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.ws.fasit.queues.FasitMessageQueueConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Configuration
@ComponentScan(basePackageClasses = {
        FasitClient.class
})
public class FasitConfig {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Bean
    public FasitMessageQueueConsumer getTpswsFasitMessageQueueQueueConsumer() {
        FasitMessageQueueConsumer consumer = new DefaultFasitMessageQueueConsumer(
                APPLICATION_NAME,
                REQUEST_QUEUE_SERVICE_RUTINE_ALIAS,
                RESPONSE_QUEUE_ALIAS,
                QUEUE_MANAGER_ALIAS
        );

        /* Inject a FasitClient object */
        beanFactory.autowireBean(consumer);
        return consumer;
    }

    @Bean
    public FasitClient getFasitClient() {
        return new FasitClient(
                BASE_URL,
                USERNAME,
                PASSWORD
        );
    }
}

package no.nav.tps.vedlikehold.consumer.ws.fasit;

import no.nav.brevogarkiv.common.fasit.FasitClient;
import no.nav.tps.vedlikehold.consumer.ws.fasit.queue.DefaultFasitMessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.ws.fasit.queue.FasitMessageQueueConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@SpringBootApplication
public class FasitConfig {

    @Autowired
    /* Used to autowire custom initialized objects */
    private AutowireCapableBeanFactory beanFactory;


    /**
     * Create a FasitMessageQueueConsumer for TPSWS and inject dependencies.
     *
     * @return an object exposing the message queues of TPSWS in all environments
     */
    @Bean
    public FasitMessageQueueConsumer getTpswsFasitMessageQueueQueueConsumer() {
        String application                  = "tpsws";
        String requestQueueAlias            = "tps.endrings.melding";
        String responseQueueAlias           = "tps.endrings.melding.svar";
        FasitMessageQueueConsumer consumer  = new DefaultFasitMessageQueueConsumer(application, requestQueueAlias, responseQueueAlias);

        /* Inject a FasitClient object */
        beanFactory.autowireBean(consumer);

        return consumer;
    }

    @Bean
    public FasitClient getFasitClient() {
        String baseUrl = "https://fasit.adeo.no/conf/";
        String username="admin";
        String password="admin";

        return new FasitClient(baseUrl, username, password);
    }

    public static void main(String[] args) {
        SpringApplication.run(FasitConfig.class, args);
    }

}

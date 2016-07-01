package no.nav.tps.vedlikehold.consumer.rs.fasit;

import no.nav.brevogarkiv.common.fasit.FasitClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
        FasitPackageMarker.class
})
@PropertySource("classpath:fasit.properties")
public class FasitConfig {

    @Autowired
    /* Used to autowire custom initialized objects */
    private AutowireCapableBeanFactory beanFactory;

    @Bean
    public FasitClient getFasitClient(
            @Value("${fasit.base-url}") String baseUrl,
            @Value("${fasit.username}") String username,
            @Value("${fasit.password}") String password) {
        return new FasitClient(baseUrl, username, password);
    }

    /**
     * Create a FasitQueueConsumer for TPSWS and inject dependencies.
     *
     * @return an object exposing the queues of TPSWS in all environments
     */
    @Bean
    public FasitQueueConsumer getTpswsFasitQueueConsumer() {
        FasitQueueConsumer consumer = new FasitQueueConsumer("tpsws");

        /* Inject a FasitClient object */
        beanFactory.autowireBean(consumer);

        return consumer;
    }

    public static void main(String[] args) {
        SpringApplication.run(FasitConfig.class, args);
    }

}

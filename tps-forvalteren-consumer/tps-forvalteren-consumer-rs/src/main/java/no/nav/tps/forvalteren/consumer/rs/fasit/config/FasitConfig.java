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

    @Bean
    public FasitClient getFasitClient() {
        return new FasitClient(
                FasitConstants.BASE_URL,
                FasitConstants.USERNAME,
                FasitConstants.PASSWORD
        );
    }
}

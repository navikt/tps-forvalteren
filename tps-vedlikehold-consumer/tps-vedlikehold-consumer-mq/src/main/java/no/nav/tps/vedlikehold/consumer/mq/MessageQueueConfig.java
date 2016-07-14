package no.nav.tps.vedlikehold.consumer.mq;


import no.nav.tps.vedlikehold.consumer.mq.factories.DefaultMessageQueueServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@SpringBootApplication
public class MessageQueueConfig {

    @Autowired
    ConfigurableApplicationContext context;

    @Autowired
    DefaultMessageQueueServiceFactory defaultMessageQueueServiceFactory;
}

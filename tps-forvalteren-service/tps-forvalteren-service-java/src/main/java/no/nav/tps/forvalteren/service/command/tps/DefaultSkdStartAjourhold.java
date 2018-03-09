package no.nav.tps.forvalteren.service.command.tps;

import java.util.Set;
import javax.jms.JMSException;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultSkdStartAjourhold implements SkdStartAjourhold {

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSkdStartAjourhold.class);
    private static final String KAN_VAERE_HVA_SOM_HELST_STRING = "start";

    @Override
    public void execute(Set<String> environments) {
        for (String environment : environments) {
            try {
                MessageQueueConsumer messageQueueConsumerStartAjour = messageQueueServiceFactory.createMessageQueueConsumer(environment, TpsConstants.REQUEST_QUEUE_START_AJOURHOLD_ALIAS);
                //messageQueueConsumerStartAjour.sendMessageAsync(KAN_VAERE_HVA_SOM_HELST_STRING);
                //messageQueueConsumerStartAjour.sendMessage(KAN_VAERE_HVA_SOM_HELST_STRING);
            } catch (JMSException e) {
                LOGGER.error("Kunne ikke starte aujorhold", e);
            }
        }
    }

}

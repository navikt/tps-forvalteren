package no.nav.tps.forvalteren.service.command.tps;

import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants;

@Service
public class DefaultSkdStartAjourhold implements SkdStartAjourhold {

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    private static final String KAN_VAERE_HVA_SOM_HELST_STRING = "start";

    @Override
    public void execute(String environment) throws JMSException {
        MessageQueueConsumer messageQueueConsumerStartAjour = messageQueueServiceFactory.createMessageQueueConsumer(environment, TpsConstants.REQUEST_QUEUE_START_AJOURHOLD_ALIAS);

        messageQueueConsumerStartAjour.sendMessageAsync(KAN_VAERE_HVA_SOM_HELST_STRING);
    }

}

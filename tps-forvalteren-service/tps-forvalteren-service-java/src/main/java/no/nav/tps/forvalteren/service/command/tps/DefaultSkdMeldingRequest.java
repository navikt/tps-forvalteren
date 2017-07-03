package no.nav.tps.forvalteren.service.command.tps;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdMeldingDefinition;
import no.nav.tps.forvalteren.service.command.authorisation.TpsAuthorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;

@Service
public class DefaultSkdMeldingRequest implements SkdMeldingRequest {

    private static final String KAN_VAERE_HVA_SOM_HELST_STRING = "start";

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Autowired
    private TpsAuthorisationService tpsAuthorisationService;

    @Override
    public String execute(String skdMelding, TpsSkdMeldingDefinition skdMeldingDefinition, String environment) throws JMSException {

        tpsAuthorisationService.authoriseRestCall(skdMeldingDefinition);

        MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumer(environment, skdMeldingDefinition.getConfig().getRequestQueue());

        messageQueueConsumer.sendMessage(skdMelding);

        MessageQueueConsumer messageQueueConsumerStartAjour = messageQueueServiceFactory.createMessageQueueConsumer(environment, TpsConstants.REQUEST_QUEUE_START_AJOURHOLD_ALIAS);

        return messageQueueConsumerStartAjour.sendMessage(KAN_VAERE_HVA_SOM_HELST_STRING);
    }
}

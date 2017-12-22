package no.nav.tps.forvalteren.service.command.tps;

import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.authorisation.ForbiddenCallHandlerService;

@Service
public class DefaultSkdMeldingRequest implements SkdMeldingRequest {

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Autowired
    private ForbiddenCallHandlerService forbiddenCallHandlerService;
    
    @Override
    public void execute(String skdMelding, TpsSkdRequestMeldingDefinition skdMeldingDefinition, String environment) throws JMSException {
        forbiddenCallHandlerService.authoriseRestCall(skdMeldingDefinition);

        MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumer(environment, skdMeldingDefinition.getConfig().getRequestQueue());

        messageQueueConsumer.sendMessage(skdMelding);
        
    }

}

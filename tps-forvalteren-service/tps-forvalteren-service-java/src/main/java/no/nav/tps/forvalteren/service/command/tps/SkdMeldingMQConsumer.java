package no.nav.tps.forvalteren.service.command.tps;

import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.authorisation.ForbiddenCallHandlerService;

@Service
public class SkdMeldingMQConsumer {

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Autowired
    private ForbiddenCallHandlerService forbiddenCallHandlerService;

    @Value("${tps.forvalteren.production.mode}")
    private boolean currentEnvironmentIsProd;

    public String sendMessage(String skdMelding, TpsSkdRequestMeldingDefinition skdMeldingDefinition, String environment) throws JMSException {

        if(currentEnvironmentIsProd){
            forbiddenCallHandlerService.authoriseRestCall(skdMeldingDefinition);
        }

        MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumer(environment, skdMeldingDefinition.getConfig().getRequestQueue(), false);

       return messageQueueConsumer.sendMessage(skdMelding);
    }
}
package no.nav.tps.forvalteren.service.command.tps.xmlmelding;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageFixedQueueServiceFactory;
import no.nav.tps.forvalteren.domain.rs.RsXmlMelding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TpsXmlSender {

    @Autowired
    private MessageFixedQueueServiceFactory messageQueueServiceFactory;

    public String sendXml(RsXmlMelding rsXmlMelding) throws Exception{

        MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumerWithFixedQueueName(rsXmlMelding.getMiljo(), rsXmlMelding.getKo());

        return messageQueueConsumer.sendMessage(rsXmlMelding.getMelding());
    }
}

package no.nav.tps.forvalteren.service.command.tps.xmlmelding;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.rs.RsXmlMelding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TpsXmlSender {

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    public String sendXml(RsXmlMelding rsXmlMelding) throws Exception{

        MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumer(rsXmlMelding.getMiljo(), rsXmlMelding.getKo());

        // TODO Gi dette litt mer logikk(!) og skill ut i egen metode
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData>");
        builder.append(rsXmlMelding.getMelding() + "</tpsPersonData>");

        return messageQueueConsumer.sendMessage(builder.toString());
    }
}

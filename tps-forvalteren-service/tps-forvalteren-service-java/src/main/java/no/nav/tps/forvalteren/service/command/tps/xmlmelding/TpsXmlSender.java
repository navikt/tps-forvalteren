package no.nav.tps.forvalteren.service.command.tps.xmlmelding;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageFixedQueueServiceFactory;
import no.nav.tps.forvalteren.domain.rs.RsXmlMelding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TpsXmlSender {

    @Autowired
    private MessageFixedQueueServiceFactory messageFixedQueueServiceFactory;

    public String sendXml(RsXmlMelding rsXmlMelding) throws Exception {

        String response = "";

        MessageQueueConsumer messageQueueConsumer = messageFixedQueueServiceFactory.createMessageQueueConsumerWithFixedQueueName(getEnvironmentFromQueueName(rsXmlMelding.getKo()), rsXmlMelding.getKo());
        response = messageQueueConsumer.sendMessage(rsXmlMelding.getMelding());

        return response;
    }

    private String getEnvironmentFromQueueName(String ko) {

        return ko.substring(3, ko.indexOf('_'));
    }

}

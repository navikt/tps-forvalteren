package no.nav.tps.forvalteren.service.command.tps.xmlmelding;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageFixedQueueServiceFactory;
import no.nav.tps.forvalteren.domain.rs.RsXmlMelding;

import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TpsXmlSender {

    @Autowired
    private MessageFixedQueueServiceFactory messageFixedQueueServiceFactory;

    public String sendXml(RsXmlMelding rsXmlMelding) throws JMSException {

        MessageQueueConsumer messageQueueConsumer = messageFixedQueueServiceFactory.createMessageQueueConsumerWithFixedQueueName(getEnvironmentFromQueueName(rsXmlMelding.getKo()), rsXmlMelding.getKo());

        return messageQueueConsumer.sendMessage(rsXmlMelding.getMelding());
    }

    private String getEnvironmentFromQueueName(String ko) {

        return ko.substring(3, ko.indexOf('_'));
    }

}

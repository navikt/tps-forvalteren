package no.nav.tps.forvalteren.service.command.tps.xmlmelding;

import javax.jms.JMSException;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageFixedQueueServiceFactory;
import no.nav.tps.forvalteren.domain.rs.RsXmlMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.utils.ContainsXmlElements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TpsXmlSender {

    @Autowired
    private MessageFixedQueueServiceFactory messageFixedQueueServiceFactory;

    @Autowired
    private SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;

    @Autowired
    private ContainsXmlElements containsXmlElements;

    public String sendXml(RsXmlMelding rsXmlMelding) throws JMSException {

        addHeaderIfSkdMelding(rsXmlMelding);

        MessageQueueConsumer messageQueueConsumer = messageFixedQueueServiceFactory.createMessageQueueConsumerWithFixedQueueName(getEnvironmentFromQueueName(rsXmlMelding.getKo()), rsXmlMelding.getKo());

        return messageQueueConsumer.sendMessage(rsXmlMelding.getMelding());
    }

    private String getEnvironmentFromQueueName(String ko) {

        return ko.substring(3, ko.indexOf('_'));
    }

    private void addHeaderIfSkdMelding(RsXmlMelding rsXmlMelding) {

        if (!containsXmlElements.execute(rsXmlMelding.getMelding())) {
            StringBuilder skdMelding = new StringBuilder(rsXmlMelding.getMelding());

            StringBuilder rsXmlMeldingMedHeader = skdAddHeaderToSkdMelding.execute(skdMelding);
            rsXmlMelding.setMelding(rsXmlMeldingMedHeader.toString());
        }
    }
}

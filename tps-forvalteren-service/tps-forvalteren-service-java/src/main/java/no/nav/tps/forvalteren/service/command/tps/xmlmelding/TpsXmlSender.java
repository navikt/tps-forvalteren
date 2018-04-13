package no.nav.tps.forvalteren.service.command.tps.xmlmelding;

import javax.jms.JMSException;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageFixedQueueServiceFactory;
import no.nav.tps.forvalteren.domain.rs.RsTpsMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.utils.ContainsXmlElements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
public class TpsXmlSender {

    @Autowired
    private MessageFixedQueueServiceFactory messageFixedQueueServiceFactory;

    @Autowired
    private SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;

    @Autowired
    private ContainsXmlElements containsXmlElements;

    public String sendTpsMelding(RsTpsMelding rsTpsMelding) throws JMSException {

        addHeaderIfSkdMelding(rsTpsMelding);

        MessageQueueConsumer messageQueueConsumer = messageFixedQueueServiceFactory.createMessageQueueConsumerWithFixedQueueName(getEnvironmentFromQueueName(rsTpsMelding.getKo()), rsTpsMelding.getKo());

        return messageQueueConsumer.sendMessage(rsTpsMelding.getMelding());
    }

    private String getEnvironmentFromQueueName(String ko) {

        return ko.substring(3, ko.indexOf('_'));
    }

    private void addHeaderIfSkdMelding(RsTpsMelding rsTpsMelding) {

        if (!containsXmlElements.execute(rsTpsMelding.getMelding())) {
            StringBuilder skdMelding = new StringBuilder(rsTpsMelding.getMelding());

            StringBuilder rsTpsMeldingMedHeader = skdAddHeaderToSkdMelding.execute(skdMelding);
            rsTpsMelding.setMelding(rsTpsMeldingMedHeader.toString());
        }
    }
}

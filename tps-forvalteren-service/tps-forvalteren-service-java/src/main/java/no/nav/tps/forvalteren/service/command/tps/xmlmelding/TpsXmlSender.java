package no.nav.tps.forvalteren.service.command.tps.xmlmelding;

import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageFixedQueueServiceFactory;
import no.nav.tps.forvalteren.domain.rs.RsTpsMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.utils.ContainsXmlElements;

@Service
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false")
public class TpsXmlSender {

    private static final Long ONE_MILLI_SECS = 1000L;

    @Autowired
    private MessageFixedQueueServiceFactory messageFixedQueueServiceFactory;

    @Autowired
    private SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;

    @Autowired
    private ContainsXmlElements containsXmlElements;

    public String sendTpsMelding(RsTpsMelding rsTpsMelding) throws JMSException {

        addHeaderIfSkdMelding(rsTpsMelding);

        MessageQueueConsumer messageQueueConsumer = messageFixedQueueServiceFactory.createMessageQueueConsumerWithFixedQueueName(
                rsTpsMelding.getMiljoe(),
                rsTpsMelding.getKo());

        return messageQueueConsumer.sendMessage(rsTpsMelding.getMelding(), rsTpsMelding.getTimeout() * ONE_MILLI_SECS);
    }

    private void addHeaderIfSkdMelding(RsTpsMelding rsTpsMelding) {

        if (!containsXmlElements.execute(rsTpsMelding.getMelding())) {
            StringBuilder skdMelding = new StringBuilder(rsTpsMelding.getMelding());

            StringBuilder rsTpsMeldingMedHeader = skdAddHeaderToSkdMelding.execute(skdMelding);
            rsTpsMelding.setMelding(rsTpsMeldingMedHeader.toString());
        }
    }
}

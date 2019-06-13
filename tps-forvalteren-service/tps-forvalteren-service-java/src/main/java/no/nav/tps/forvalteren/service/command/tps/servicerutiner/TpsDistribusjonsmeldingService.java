package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static java.lang.String.format;
import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static no.nav.tps.xjc.ctg.domain.s302.SRnavnType.FS_04_HENDELSE_OVERSIKT_O;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.jms.JMSException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.DefaultSkdGetHeaderForSkdMelding;
import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;

@Slf4j
@Service
public class TpsDistribusjonsmeldingService {

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Autowired
    private Marshaller tpsDataS302Marshaller;

    @Autowired
    private Unmarshaller tpsDataS302Unmarshaller;

    @Autowired
    private DefaultSkdGetHeaderForSkdMelding skdGetHeaderForSkdMelding;

    public TpsPersonData getDistribusjonsmeldinger(TpsPersonData tpsPersonData, String environment) {

        setServiceRoutineMeta(tpsPersonData);

        String xmlResponse = sendTpsRequest(tpsPersonData, environment);

        try {
            return (TpsPersonData) tpsDataS302Unmarshaller.unmarshal(new StringReader(xmlResponse));
        } catch (JAXBException e) {
            log.error(e.getMessage(), e);
            throw new TpsfTechnicalException(format("Feil ved konvertering av XML-melding fra TPS. %s", e.getMessage()));
        }
    }

    public String sendDetailedSkdMessageToTps(String message, String env, String queueName) {

        try {
            MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumer(env, queueName, true);

            return messageQueueConsumer.sendMessage(skdGetHeaderForSkdMelding.prependHeader(message), 100);

        } catch (JMSException e) {
            log.error(e.getMessage(), e);
            throw new TpsfTechnicalException(format("Feil ved sending til TPS %s, se logg!", e.getMessage()), e);
        }
    }

    public String sendDetailedDistribusjonMessage(String message, String env, String queueName) {

        try {
            MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumer(env, queueName, true);

            return messageQueueConsumer.sendMessage(message, 100);

        } catch (JMSException e) {
            log.error(e.getMessage(), e);
            throw new TpsfTechnicalException(format("Feil ved sending til TPS %s, se logg!", e.getMessage()), e);
        }
    }

    private String sendTpsRequest(TpsPersonData tpsRequest, String env) {
        try {
            MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumer(env, REQUEST_QUEUE_SERVICE_RUTINE_ALIAS, false);

            Writer xmlWriter = new StringWriter();
            tpsDataS302Marshaller.marshal(tpsRequest, xmlWriter);

            return messageQueueConsumer.sendMessage(xmlWriter.toString(), 30_000);

        } catch (JMSException | JAXBException e) {
            log.error(e.getMessage(), e);
            throw new TpsfTechnicalException(format("Feil ved sending til TPS %s, se logg!", e.getMessage()), e);
        }
    }

    private void setServiceRoutineMeta(TpsPersonData tpsPersonData) {
        tpsPersonData.getTpsServiceRutine().setServiceRutinenavn(FS_04_HENDELSE_OVERSIKT_O);
        tpsPersonData.getTpsServiceRutine().setAksjonsKode("A");
        tpsPersonData.getTpsServiceRutine().setAksjonsKode2("0");
    }
}

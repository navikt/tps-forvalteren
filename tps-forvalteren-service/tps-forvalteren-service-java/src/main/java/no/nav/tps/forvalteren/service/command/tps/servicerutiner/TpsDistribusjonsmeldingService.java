package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static no.nav.tps.xjc.ctg.domain.s302.SRnavnType.FS_04_HENDELSE_OVERSIKT_O;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.DefaultSkdGetHeaderForSkdMelding;
import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;

@Slf4j
@Service
public class TpsDistribusjonsmeldingService {

    private static final long REQUEST_TIMEOUT = 30_000;
    private static final String REQUEST_TIMEOUT_KEY = "avspiller.request.timeout";
    private static final String XML_CONVERSION_ERROR = "avspiller.response.xml.conversion";
    private static final String TPS_SEND_ERROR = "avspiller.request.tps.sending";

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Autowired
    private Marshaller tpsDataS302Marshaller;

    @Autowired
    private Unmarshaller tpsDataS302Unmarshaller;

    @Autowired
    private DefaultSkdGetHeaderForSkdMelding skdGetHeaderForSkdMelding;

    @Autowired
    private MessageProvider messageProvider;

    public TpsPersonData getDistribusjonsmeldinger(TpsPersonData tpsPersonData, String environment, Long timeout) {

        setServiceRoutineMeta(tpsPersonData);

        String xmlResponse = sendTpsRequest(tpsPersonData, environment, timeout);

        if (isBlank(xmlResponse)) {
            throw new NotFoundException(messageProvider.get(REQUEST_TIMEOUT_KEY, environment,
                    tpsPersonData.getTpsServiceRutine().getFraDato(), tpsPersonData.getTpsServiceRutine().getTilDato()));
        }

        try {
            return (TpsPersonData) tpsDataS302Unmarshaller.unmarshal(new StringReader(xmlResponse));
        } catch (JAXBException e) {
            log.error(e.getMessage(), e);
            throw new TpsfTechnicalException(messageProvider.get(XML_CONVERSION_ERROR, e.getMessage()), e);
        }
    }

    public String sendDetailedMessageToTps(String message, String env, String queueName, boolean includeHeader) {

        try {
            MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumer(env, queueName, true);

            return messageQueueConsumer.sendMessage(includeHeader ? skdGetHeaderForSkdMelding.prependHeader(message) : message, 100);

        } catch (JMSException | RuntimeException e) {
            log.error(e.getMessage(), e);
            return (messageProvider.get(TPS_SEND_ERROR, e.getMessage()));
        }
    }

    private String sendTpsRequest(TpsPersonData tpsRequest, String env, Long timeout) {
        try {
            MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumer(env, REQUEST_QUEUE_SERVICE_RUTINE_ALIAS, false);

            Writer xmlWriter = new StringWriter();
            tpsDataS302Marshaller.marshal(tpsRequest, xmlWriter);

            return messageQueueConsumer.sendMessage(xmlWriter.toString(), nonNull(timeout) ? timeout * 1000 : REQUEST_TIMEOUT);

        } catch (JMSException | JAXBException e) {
            log.error(e.getMessage(), e);
            throw new TpsfTechnicalException(messageProvider.get(TPS_SEND_ERROR, e.getMessage()), e);
        }
    }

    private void setServiceRoutineMeta(TpsPersonData tpsPersonData) {
        tpsPersonData.getTpsServiceRutine().setServiceRutinenavn(FS_04_HENDELSE_OVERSIKT_O);
        tpsPersonData.getTpsServiceRutine().setAksjonsKode("A");
        tpsPersonData.getTpsServiceRutine().setAksjonsKode2("0");
    }
}

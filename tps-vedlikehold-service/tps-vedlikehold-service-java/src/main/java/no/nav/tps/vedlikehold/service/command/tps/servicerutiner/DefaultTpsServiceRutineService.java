package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import java.io.IOException;

import javax.jms.JMSException;

import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.xml.XmlMapper;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Service
public class DefaultTpsServiceRutineService implements TpsServiceRutineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTpsServiceRutineService.class);
    private static final String XML_PROPERTIES_PREFIX  = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData>";
    private static final String XML_PROPERTIES_POSTFIX = "</tpsPersonData>";

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Autowired
    private XmlMapper xmlMapper;

    /**
     * Send a request to TPS using asynchronous message queues
     *
     * @param tpsRequestServiceRoutine TPS request object
     *
     * @return A String representing the XML-response from TPS
     *
     * @throws JMSException failed to send the message
     */
    @Override
    public String execute(TpsRequestServiceRoutine tpsRequestServiceRoutine) throws IOException, JMSException {
        try {
            String requestMessage = XML_PROPERTIES_PREFIX + xmlMapper.writeValueAsString(tpsRequestServiceRoutine) + XML_PROPERTIES_POSTFIX;     //TODO: This class shouldnt be responsible for message construction

            MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueService( tpsRequestServiceRoutine.getEnvironment() );
            String responseXml = messageQueueConsumer.sendMessage(requestMessage);
            return responseXml;
        } catch (JMSException exception) {
            LOGGER.error("Failed to connect to MQ with exception: {}", exception.toString());
            throw exception;
        }
    }
}

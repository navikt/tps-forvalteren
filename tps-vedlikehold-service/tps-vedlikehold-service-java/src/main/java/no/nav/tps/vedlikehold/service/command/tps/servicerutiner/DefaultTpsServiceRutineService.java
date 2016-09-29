package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import java.io.IOException;
import java.util.Map;

import javax.jms.JMSException;

import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;

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
     * @return an object wrapping the raw XML response, and the XML represented by an object
     *
     * @throws JMSException failed to send the message
     * @throws IOException failed to convert the response XML to an object
     */
    @Override
    public ServiceRoutineResponse execute(TpsRequestServiceRoutine tpsRequestServiceRoutine) throws IOException, JMSException {
        try {
            String requestMessage = XML_PROPERTIES_PREFIX + xmlMapper.writeValueAsString(tpsRequestServiceRoutine) + XML_PROPERTIES_POSTFIX;     //TODO: This class shouldnt be responsible for message construction

            /* Send message to TPS and handle the received data */
            MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueService( tpsRequestServiceRoutine.getEnvironment() );

            String responseXml = messageQueueConsumer.sendMessage(requestMessage);

            Object responseData = xmlMapper.readValue(responseXml, Map.class);          //TODO Map to custom object

            return new ServiceRoutineResponse(responseXml, responseData);
        } catch (IOException exception) {
            LOGGER.error("Failed to convert TPS during XML marshalling with exception: {}", exception.toString());
            throw exception;
        } catch (JMSException exception) {
            LOGGER.error("Failed to connect to MQ with exception: {}", exception.toString());
            throw exception;
        }
    }
}

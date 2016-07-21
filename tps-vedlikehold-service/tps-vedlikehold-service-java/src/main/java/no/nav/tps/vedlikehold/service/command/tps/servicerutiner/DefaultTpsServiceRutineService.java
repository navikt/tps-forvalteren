package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.domain.service.ServiceRutineResponse;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories.DefaultServiceRutineMessageFactory;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories.DefaultServiceRutineMessageFactoryStrategy;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories.ServiceRutineMessageFactory;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories.ServiceRutineMessageFactoryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.Map;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Service
public class DefaultTpsServiceRutineService implements TpsServiceRutineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTpsServiceRutineService.class);

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    private XmlMapper xmlMapper;

    private ServiceRutineMessageFactory serviceRutineMessageFactory;


    public DefaultTpsServiceRutineService() {
        this.xmlMapper = new XmlMapper();
        this.serviceRutineMessageFactory = new DefaultServiceRutineMessageFactory();
    }


    /**
     * Send a request to TPS using asynchronous MQs
     *
     * @param serviceRutine name of the service rutine to be executed
     * @param parameters parameters needed to run the service rutine (as defined in document GR.8.1.1-0071)
     * @param environment the environment in which to contact TPS
     * @return an object wrapping the raw XML response, and the XML represented by an object
     * @throws Exception failed to send the message
     */

    @Override
    public ServiceRutineResponse execute(String serviceRutine,
                                         Map<String, Object> parameters,
                                         String environment) throws IOException, JMSException {

        ServiceRutineMessageFactoryStrategy messageFactoryStrategy = new DefaultServiceRutineMessageFactoryStrategy(serviceRutine, parameters);

        String requestMessage = serviceRutineMessageFactory.createMessage(messageFactoryStrategy);

        /* Prepare the serialized and raw response */
        String responseMessage = null;
        Object responseData = null;

        try {
            MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueService(environment);

            responseMessage = messageQueueConsumer.sendMessage(requestMessage);

            responseData = xmlMapper.readValue(responseMessage, Map.class);
        } catch (IOException exception) {
            LOGGER.error("Failed to convert TPS response XML to an object with exception: {}", exception.toString());

            throw exception;
        } catch (JMSException exception) {
            LOGGER.error("Failed to connect to MQ with exception: {}", exception.toString());

            throw exception;
        }

        /* Populate a response object and return */
        ServiceRutineResponse response = new ServiceRutineResponse();

        response.setXml(responseMessage);
        response.setData(responseData);

        return response;
    }
}

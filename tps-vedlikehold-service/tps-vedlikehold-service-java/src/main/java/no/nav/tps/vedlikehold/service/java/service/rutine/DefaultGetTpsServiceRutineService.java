package no.nav.tps.vedlikehold.service.java.service.rutine;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.consumer.mq.services.MessageQueueService;
import no.nav.tps.vedlikehold.domain.service.ServiceRutineResponse;
import no.nav.tps.vedlikehold.service.java.service.rutine.factories.DefaultServiceRutineMessageFactory;
import no.nav.tps.vedlikehold.service.java.service.rutine.factories.DefaultServiceRutineMessageFactoryStrategy;
import no.nav.tps.vedlikehold.service.java.service.rutine.factories.ServiceRutineMessageFactory;
import no.nav.tps.vedlikehold.service.java.service.rutine.factories.ServiceRutineMessageFactoryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.Map;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Service
public class DefaultGetTpsServiceRutineService implements GetTpsServiceRutineService {

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    private XmlMapper xmlMapper;

    private ServiceRutineMessageFactory serviceRutineMessageFactory;


    public DefaultGetTpsServiceRutineService() {
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
    public ServiceRutineResponse execute(String serviceRutine,
                                         Map<String, Object> parameters,
                                         String environment) {

        ServiceRutineMessageFactoryStrategy messageFactoryStrategy = new DefaultServiceRutineMessageFactoryStrategy(serviceRutine, parameters);

        String requestMessage = serviceRutineMessageFactory.createMessage(messageFactoryStrategy);

        /* Prepare the serialized and raw response */
        String responseMessage = null;
        Object responseData = null;

        try {
            MessageQueueService messageQueueService = messageQueueServiceFactory.createMessageQueueService(environment);

            responseMessage = messageQueueService.sendMessage(requestMessage);

            responseData = xmlMapper.readValue(responseMessage, Map.class);
        } catch (IOException exception) {
            responseMessage = exception.getMessage();
            exception.printStackTrace();                                                //TODO: Log exceptions
        } catch (JMSException exception) {
            responseData = exception.getMessage();
            exception.printStackTrace();                                                //TODO: Log exceptions
        }

        /* Populate a response object and return */
        ServiceRutineResponse response = new ServiceRutineResponse();

        response.setXml(responseMessage);
        response.setData(responseData);

        return response;
    }
}

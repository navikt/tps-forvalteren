package no.nav.tps.vedlikehold.service.java.service.rutine;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.consumer.mq.services.MessageQueueService;
import no.nav.tps.vedlikehold.domain.service.ServiceRutineResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.Map;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Service
public class ServiceRutineService {

    private  static final String TPS_REQUEST_MESSAGE_FILE_NAME = "TpsServiceRutineMessage";

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    MessageQueueServiceFactory messageQueueServiceFactory;

    public ServiceRutineResponse getResponseForServiceRutine(String serviceRutine, Map<String, ?> parameters, String environment) throws Exception {

        String requestMessage = requestMessageForService(serviceRutine, parameters);

        /* Prepare the serialized and raw response */
        String responseMessage = responseForMessage(requestMessage, environment);

        Object responseData = convertToDictionary(responseMessage);

        /* Populate a response object and return */
        ServiceRutineResponse response = new ServiceRutineResponse();

        response.setXml(responseMessage);
        response.setData(responseData);

        return response;
    }

    /** Convert an XML string to a dictionary / JSON object */
    private Map convertToDictionary(String xmlString) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();

        return xmlMapper.readValue(xmlString, Map.class);
    }

    /** Send request to the request queue and wait for a response */
    private String responseForMessage(String requestMessage, String environment) throws JMSException {
        MessageQueueService messageQueueService = messageQueueServiceFactory.createMessageQueueService(environment);

        return messageQueueService.sendMessage(requestMessage);
    }

    private String requestMessageForService(String serviceName, Map<String, ?> parameters) {
        IContext templateContext = contextForService(serviceName, parameters);

        return templateEngine.process(TPS_REQUEST_MESSAGE_FILE_NAME, templateContext);
    }

    private IContext contextForService(String serviceName, Map<String, ?> parameters) {
        Context context = new Context();

        context.setVariable("serviceRutinenavn", serviceName.toUpperCase());
        context.setVariables(parameters);

        return context;
    }
}

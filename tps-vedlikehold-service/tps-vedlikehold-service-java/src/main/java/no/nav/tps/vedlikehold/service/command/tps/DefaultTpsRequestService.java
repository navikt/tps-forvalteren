package no.nav.tps.vedlikehold.service.command.tps;

import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConstants;
import no.nav.tps.vedlikehold.domain.service.command.tps.Request;
import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import no.nav.tps.vedlikehold.service.command.tps.transformation.TransformationService;
import no.nav.tps.vedlikehold.service.command.tps.utils.TpsRequestXmlCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.io.IOException;


@Service
public class DefaultTpsRequestService implements TpsRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTpsRequestService.class);

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Autowired
    private TpsRequestXmlCreator tpsRequestXmlCreator;

    @Autowired
    private TransformationService transformationService;

    /**
     * Send a request to TPS using asynchronous message queues
     *
     * @param tpsRequest TPS request object
     * @return A String representing the XML-response from TPS
     * @throws JMSException failed to send the message
     */
    @Override
    public Response executeServiceRutineRequest(TpsRequestServiceRoutine tpsRequest, TpsServiceRoutine serviceRoutine) throws IOException, JMSException {
        try {
            MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueService(tpsRequest.getEnvironment(), FasitConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS);
            String xmlRequestMessage = tpsRequestXmlCreator.createXmlTpsRequestServiceRutine(tpsRequest);

            Request request = new Request();
            request.setXml(xmlRequestMessage);
            request.setRoutineRequest(tpsRequest);

            transformationService.transform(request, serviceRoutine);

            String responseXml = messageQueueConsumer.sendMessage(request.getXml());
            Response response = new Response();
            response.setXml(responseXml);

//            securityServices.performPost(response, serviceRoutine);
            transformationService.transform(response, serviceRoutine);

            return response;
        } catch (JMSException exception) {
            LOGGER.error("Failed to connect to MQ with exception: {}", exception.toString());
            throw exception;
        }
    }
/*
    @Override
    public String executeEndringsmeldingRequest(TpsRequestEndringsmelding tpsRequest, TpsSystemInfo tpsSystemInfo) throws IOException, JMSException {
        try {

            //TODO demolish

            MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueService(tpsRequest.getEnvironment(), FasitConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS);


            String xmlRequestMessage = tpsRequestXmlCreator.createXmlTpsRequestEndringsmelding(tpsRequest, tpsSystemInfo);



            String responseXml = messageQueueConsumer.sendMessage(xmlRequestMessage);
            return responseXml;
        } catch (JMSException exception) {
            LOGGER.error("Failed to connect to MQ with exception: {}", exception.toString());
            throw exception;
        }
    }
    */
}

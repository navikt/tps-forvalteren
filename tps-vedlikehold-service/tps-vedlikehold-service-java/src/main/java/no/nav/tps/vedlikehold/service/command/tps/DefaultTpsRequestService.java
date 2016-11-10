package no.nav.tps.vedlikehold.service.command.tps;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConstants;
import no.nav.tps.vedlikehold.domain.service.command.tps.Request;
import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.service.command.tps.transformation.TransformationService;
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
    private XmlMapper xmlMapper;

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Autowired
    private TransformationService transformationService;

    @Autowired
    private TpsAuthorisationService tpsAuthorisationService;

    @Autowired
    private MessageProvider messageProvider;

    /**
     * Send a request to TPS using asynchronous message queues
     *
     * @param tpsRequest TPS request object
     * @return A String representing the XML-response from TPS
     * @throws JMSException failed to send the message
     */
    @Override
    public Response executeServiceRutineRequest(TpsServiceRoutineRequest tpsRequest, TpsServiceRoutineDefinition serviceRoutine, TpsRequestContext context) throws JMSException, IOException {
        //TODO innkommenter når ting funker :3

        // Denne må alltid bli gjort 1 gang uansett uavhenging av om man får mange resultater eller 1.
        tpsAuthorisationService.authoriseRestCall(serviceRoutine, context.getEnvironment(), context.getUser());

        //TODO hent kø som melding skal sendes på i resolver
        MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueService(context.getEnvironment(), FasitConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS);

        String xml = xmlMapper.writeValueAsString(tpsRequest);

        Request request = new Request();
        request.setXml(xml);
        request.setRoutineRequest(tpsRequest);
        request.setContext(context);

        transformationService.transform(request, serviceRoutine);

        String responseXml = messageQueueConsumer.sendMessage(request.getXml());

        Response response = new Response();
        response.setXml(responseXml);

        transformationService.transform(response, serviceRoutine);

        return response;
    }

}

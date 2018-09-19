package no.nav.tps.forvalteren.service.command.tps;

import java.io.IOException;
import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.xml.XmlMapper;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.Request;
import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineHentByFnrRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.service.command.authorisation.ForbiddenCallHandlerService;
import no.nav.tps.forvalteren.service.command.tps.transformation.TransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TpsRequestService {

    @Autowired
    private XmlMapper xmlMapper;

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Autowired
    private TransformationService transformationService;

    @Autowired
    private ForbiddenCallHandlerService forbiddenCallHandlerService;

    @Value("${tps.forvalteren.production-mode}")
    private boolean currentEnvironmentIsProd;

    public Response executeServiceRutineRequest(TpsServiceRoutineRequest tpsRequest, TpsServiceRoutineDefinitionRequest serviceRoutine, TpsRequestContext context, long timeout)
            throws JMSException, IOException {

        if(currentEnvironmentIsProd){
            forbiddenCallHandlerService.authoriseRestCall(serviceRoutine);
        }

        MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumer(context.getEnvironment(), serviceRoutine.getConfig().getRequestQueue());

        if(currentEnvironmentIsProd && (tpsRequest instanceof TpsServiceRoutineHentByFnrRequest) ){
            forbiddenCallHandlerService.authorisePersonSearch(serviceRoutine,((TpsServiceRoutineHentByFnrRequest) tpsRequest).getFnr());
        }

        tpsRequest.setServiceRutinenavn(removeTestdataFromServicerutinenavn(tpsRequest.getServiceRutinenavn()));
        String xml = xmlMapper.writeValueAsString(tpsRequest);

        Request request = new Request(xml, tpsRequest, context);
        transformationService.transform(request, serviceRoutine);

        String responseXml = messageQueueConsumer.sendMessage(request.getXml(), timeout);

        Response response = new Response(responseXml, context, serviceRoutine);
        transformationService.transform(response, serviceRoutine);

        return response;
    }

    private String removeTestdataFromServicerutinenavn(String serviceRutinenavn){

        if(serviceRutinenavn.endsWith("TESTDATA")){
            return serviceRutinenavn.replace("-TESTDATA", "");
        }
        return serviceRutinenavn;
    }

}

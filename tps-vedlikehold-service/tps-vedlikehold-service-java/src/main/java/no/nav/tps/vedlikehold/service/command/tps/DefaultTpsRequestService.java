package no.nav.tps.vedlikehold.service.command.tps;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.domain.service.command.tps.Request;
import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.config.TpsConstants;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.vedlikehold.service.command.testdata.SkdMeldingFormatter;
import no.nav.tps.vedlikehold.service.command.tps.transformation.TransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.io.IOException;

@Service
public class DefaultTpsRequestService implements TpsRequestService {

    @Autowired
    private XmlMapper xmlMapper;

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Autowired
    private TransformationService transformationService;

    @Autowired
    private TpsAuthorisationService tpsAuthorisationService;

    @Autowired
    private SkdMeldingFormatter skdMeldingFormatter;

    /**
     * Send a request to TPS using asynchronous message queues
     *
     * @param tpsRequest TPS request object
     * @return A String representing the XML-response from TPS
     * @throws JMSException failed to send the message
     */
    @Override
    public Response executeServiceRutineRequest(TpsServiceRoutineRequest tpsRequest, TpsServiceRoutineDefinition serviceRoutine, TpsRequestContext context) throws JMSException, IOException {

        tpsAuthorisationService.authoriseRestCall(serviceRoutine, context.getEnvironment(), context.getUser());

        MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueService(context.getEnvironment(), serviceRoutine.getConfig().getRequestQueue());

        String xml = xmlMapper.writeValueAsString(tpsRequest);

        Request request = new Request(xml, tpsRequest, context);
        transformationService.transform(request, serviceRoutine);

        String responseXml = messageQueueConsumer.sendMessage(request.getXml());

        Response response = new Response(responseXml, context, serviceRoutine);
        transformationService.transform(response, serviceRoutine);

        return response;
    }

    @Override
    public void executeSkdMeldingRequest(String skdMelding, TpsRequestContext context) throws JMSException {

        MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueService(context.getEnvironment(), TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS);

        // For å teste. Dette er en ferdig melding.
        skdMelding = "111049338412016111112192212420161104 00000000                                                                                                                                                                                                                                 00000000                        000000000000201453554600000000200000000000000000000000000000                                                  000201611042016102807063130000269903000SPORTSVEIEN              O                         32240003                                                                                          0000000000000000000000000000000000000000000000000000000000000 00000000 00000000 00000000 000000000000000000000000000                                                  00000000000000                                                  00000000000000000000000000000000000000000                   0000   0000 00000000 003955H0101000000000000F02MAU  00050000                                                                                                                        00000000000 00000000 0413000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000 00000000                                                                                                                                ";
        String headerSkdMelding = skdMeldingFormatter.createHeaderToSkdMelding(skdMelding); //TODO Flytt til controller like gjerne.

        //Sneder SKD melding til sfe_ajourfor_skd
        String response = messageQueueConsumer.sendMessage(headerSkdMelding+skdMelding);
//
//        // Ajourfor meldingen som ligger i sfe_ajourfor_skd. TODO Kan kanskje bare bruke samme messageQueueConsumer??
        MessageQueueConsumer messageQueueConsumerStartAjour = messageQueueServiceFactory.createMessageQueueService(context.getEnvironment(), TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS);
        messageQueueConsumerStartAjour.setRequestQueue(TpsConstants.REQUEST_QUEUE_START_AJOURHOLD_ALIAS_FULL);
//         //Set her Køen manuelt.
//
        String responseAjour = messageQueueConsumerStartAjour.sendMessage("Start");   // Her kan det stå hva som helst.
        System.out.println("Response: " + responseAjour);
    }

    /* Success melding!! "t4".  Var med header og hele pakka. 16.01  08:13

281216967802016122820352110120161228100000000TESTIT                                            SAIMATESTI                                                                                                                                                                     000000000301OSLO UNIVERSITETSSYK000000000001607803055420161228300000000010000000000000000000                                                  000201612280000000003012036900440000000MEKLENBORGÅSEN           O                         12741507                                                                                          0000000000000000000000000000000000000000000000000000000000000 00000000 00000000D20161228 000000000000000001128025843                                                  00016078030554                                                  000000000000000000000000000000000000000001M                 0000   0000 00000000 000019     000000000000MFR     01040000                                                                                                                        00000000000 00000000 3310000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000 00000000

      */

}

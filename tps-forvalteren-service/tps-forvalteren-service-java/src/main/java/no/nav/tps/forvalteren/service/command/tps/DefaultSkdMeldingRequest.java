package no.nav.tps.forvalteren.service.command.tps;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdOpprettSkdMeldingMedHeaderOginnhold;
import no.nav.tps.forvalteren.service.command.testdata.skd.utils.PersonToSkdParametersMapper;
import no.nav.tps.forvalteren.service.command.tps.transformation.TransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.util.Map;

@Service
public class DefaultSkdMeldingRequest implements SkdMeldingRequest {

    @Autowired
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Autowired
    private TransformationService transformationService;

    @Autowired
    private TpsAuthorisationService tpsAuthorisationService;

    @Autowired
    private SkdOpprettSkdMeldingMedHeaderOginnhold skdOpprettSkdMeldingMedHeaderOginnhold;

    @Autowired
    private SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;

    @Autowired
    private PersonToSkdParametersMapper skdParametersMapper;

    @Override
    public String execute(String skdMelding, TpsSkdMeldingDefinition skdMeldingDefinition, TpsRequestContext context) throws JMSException {

        MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumer(context.getEnvironment(), TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS);

        Person per = new Person();
        per.setIdent("07029828428");
        per.setFornavn("Stor");
        per.setEtternavn("Kopp");
        Map<String,String> skdParam = skdParametersMapper.execute(per);

        String skdMeldingEkte = skdOpprettSkdMeldingMedHeaderOginnhold.execute(skdParam);

        // For å teste. Dette er en ferdig melding.
        skdMelding = "111049338412016111112192212420161104 00000000                                                                                                                                                                                                                                 00000000                        000000000000201453554600000000200000000000000000000000000000                                                  000201611042016102807063130000269903000SPORTSVEIEN              O                         32240003                                                                                          0000000000000000000000000000000000000000000000000000000000000 00000000 00000000 00000000 000000000000000000000000000                                                  00000000000000                                                  00000000000000000000000000000000000000000                   0000   0000 00000000 003955H0101000000000000F02MAU  00050000                                                                                                                        00000000000 00000000 0413000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000 00000000                                                                                                                                ";

        StringBuilder stringBuilder = new StringBuilder(skdMelding);
        skdAddHeaderToSkdMelding.execute(stringBuilder); //TODO Flytt til controller like gjerne.

        //String skd = skdOpprettSkdMeldingMedHeaderOginnhold.execute()

        //Sneder SKD melding til sfe_ajourfor_skd
        String response = messageQueueConsumer.sendMessage(skdMeldingEkte);
//
//        // Ajourfor meldingen som ligger i sfe_ajourfor_skd. TODO Kan kanskje bare bruke samme messageQueueConsumer??
        MessageQueueConsumer messageQueueConsumerStartAjour = messageQueueServiceFactory.createMessageQueueConsumer(context.getEnvironment(), TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS);

        String ajour = "QA." + context.getEnvironment().toUpperCase() + "_412." + TpsConstants.REQUEST_QUEUE_START_AJOURHOLD_ALIAS;
        messageQueueConsumerStartAjour.setRequestQueue(ajour);
//         //Set her Køen manuelt.
//
        String responseAjour = messageQueueConsumerStartAjour.sendMessage("Start");   // Her kan det stå hva som helst.

        return responseAjour;
    }
}

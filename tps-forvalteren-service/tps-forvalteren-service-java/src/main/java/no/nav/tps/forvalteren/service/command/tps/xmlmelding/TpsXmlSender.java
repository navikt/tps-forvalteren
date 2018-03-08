package no.nav.tps.forvalteren.service.command.tps.xmlmelding;

import java.util.HashSet;
import java.util.Set;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageFixedQueueServiceFactory;
import no.nav.tps.forvalteren.domain.rs.RsXmlMelding;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.tps.SkdStartAjourhold;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TpsXmlSender {

    @Autowired
    private MessageFixedQueueServiceFactory messageQueueServiceFactory;

    @Autowired
    FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @Autowired
    GetEnvironments getEnvironments;

    @Autowired SkdStartAjourhold skdStartAjourhold;

    public String sendXml(RsXmlMelding rsXmlMelding) throws Exception{

        String response = "";

        MessageQueueConsumer messageQueueConsumer = messageQueueServiceFactory.createMessageQueueConsumerWithFixedQueueName(getEnvironmentFromQueueName(rsXmlMelding.getKo()), rsXmlMelding.getKo());
        response = messageQueueConsumer.sendMessage(rsXmlMelding.getMelding());

        switch(getMessageTypeFromQueueName(rsXmlMelding.getKo())) {
            case "SFE_ENDRINGSMELDING":
                startAjourhold();
                break;
            case "TPS_FORESPORSEL_XML_O":
                break;
            default:
                break;
        }

        return response;
    }

    private void startAjourhold(){
        Set<String> environments = filterEnvironmentsOnDeployedEnvironment.execute(getEnvironments.getEnvironmentsFromFasit("tpsws"));
        skdStartAjourhold.execute(new HashSet<>(environments));
    }

    private String getEnvironmentFromQueueName(String ko){
        return ko.substring(3, ko.indexOf("_"));
    }

    private String getMessageTypeFromQueueName(String ko){
        return ko.substring(ko.lastIndexOf(".")+1, ko.length());
    }


}

package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MQConnectionFactoryByFasit;
import no.nav.tps.forvalteren.domain.rs.RsTpsMelding;

@RequestMapping(value = "api/v1")
@RestController
public class MqDispatcherController {

    private static final String REST_SERVICE_NAME = "service";

    @Autowired
    private MQConnectionFactoryByFasit mqConnectionFactoryByFasit;

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "sendXmlMelding") })
    @RequestMapping(value = "/mqdispatch", method = RequestMethod.POST)
    @ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
    public String putMsgOnQueue(@RequestBody RsTpsMelding tpsMelding,
            @RequestParam("name") String name,
            @RequestParam("hostname") String hostname,
            @RequestParam("port") String port,
            @RequestParam("queueName") String queueName,
            @RequestParam("channel") String channel) throws Exception{

        MessageQueueConsumer messageQueueConsumer = mqConnectionFactoryByFasit.createMessageQueueConsumer(name, hostname, port, queueName, channel);

        String msg = messageQueueConsumer.sendMessage(tpsMelding.getMelding());

        return msg;
    }

}

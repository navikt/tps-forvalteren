package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.common.logging.LogExceptions;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MQConnectionFactoryCustomized;
import no.nav.tps.forvalteren.domain.rs.RsPureXmlMessageResponse;
import no.nav.tps.forvalteren.domain.rs.RsTpsMelding;

@RequestMapping(value = "api/v1")
@RestController
@RequiredArgsConstructor
public class MqDispatcherController {

    private final MQConnectionFactoryCustomized mqConnectionFactoryCustomized;

    @LogExceptions
    @RequestMapping(value = "/mqdispatch", method = RequestMethod.POST)
    @ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
    public RsPureXmlMessageResponse putMsgOnQueue(@RequestBody RsTpsMelding tpsMelding,
            @RequestParam("name") String name,
            @RequestParam("hostname") String hostname,
            @RequestParam("port") Integer port,
            @RequestParam("queueName") String queueName,
            @RequestParam("channel") String channel) throws Exception {

        MessageQueueConsumer messageQueueConsumer = mqConnectionFactoryCustomized.createMessageQueueConsumer(name, hostname, port, queueName, channel);

        String msg = messageQueueConsumer.sendMessage(tpsMelding.getMelding());

        RsPureXmlMessageResponse response = new RsPureXmlMessageResponse();
        response.setXml(msg);

        return response;

    }

}

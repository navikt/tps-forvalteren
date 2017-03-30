package no.nav.tps.vedlikehold.service.command.tps;

import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class PingTps implements Command {

    private static final String PING_MESSAGE = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData xmlns=\"http://www.rtv.no/NamespaceTPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.rtv.no/NamespaceTPS H:\\SYSTEM~1\\SYSTEM~4\\FS03TP~1\\TPSDAT~1.XSD\"><tpsServiceRutine><serviceRutinenavn>FS03-OTILGANG-TILSRTPS-O</serviceRutinenavn></tpsServiceRutine></tpsPersonData>";

    @Autowired
    private MessageQueueConsumer consumer;

    public void execute() throws Exception {
        String response = consumer.sendMessage(PING_MESSAGE);

        if (isEmpty(response) || !response.contains("TPS OK") ) {
            throw new RuntimeException();
        }
    }
}
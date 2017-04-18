package no.nav.tps.vedlikehold.provider.rs.api.v1.config;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;

import static no.nav.tps.vedlikehold.provider.rs.api.v1.config.RsProviderIntegrationTestConfig.TPS_TEST_REQUEST_QUEUE;

@Component
public class RequestQueueListener {

    @Inject
    private Queue responseQueue;

    @Inject
    private JmsTemplate jmsTemplate;

    private String responseMessageAsString;

    public void setResponseMessage(String responseMessage) {
        responseMessageAsString = responseMessage;
    }

    @JmsListener(destination = TPS_TEST_REQUEST_QUEUE)
    public void onMessageReceived(TextMessage textMessage) {
        try {
            addMeldingToQueue(responseMessageAsString, textMessage.getJMSMessageID());
            setResponseMessage(null);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void addMeldingToQueue(final String meldingXml, final String correlationId) {
        jmsTemplate.send(responseQueue, session -> {
            final TextMessage msg = session.createTextMessage(meldingXml);
            msg.setJMSCorrelationID(correlationId);
            return msg;
        });
    }

    public Queue getResponseQueue() {
        return responseQueue;
    }
}

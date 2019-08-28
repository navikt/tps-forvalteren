package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static no.nav.tps.forvalteren.domain.rs.Meldingsformat.AJOURHOLDSMELDING;
import static no.nav.tps.forvalteren.domain.rs.Meldingsformat.DISTRIBUSJONSMELDING;
import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.io.StringWriter;
import javax.jms.JMSException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;
import no.nav.tps.xjc.ctg.domain.s302.TpsServiceRutineType;

@RunWith(MockitoJUnitRunner.class)
public class TpsDistribusjonsmeldingServiceTest {

    private static final String ENV = "u5";
    private static final String MESSAGE = "MyMessage";
    private static final String QUEUE_NAME = "MyQueueName";
    private static final String TPS_SEND_ERROR = "avspiller.request.tps.sending";
    private static final String SEND_ERROR = "Sending of message to TPS failed";

    @Mock
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Mock
    private JAXBContext tpsDataS302Context;

    @Mock
    private MessageProvider messageProvider;

    @InjectMocks
    private TpsDistribusjonsmeldingService tpsDistribusjonsmeldingService;

    @Mock
    private MessageQueueConsumer messageQueueConsumer;

    @Mock
    private Unmarshaller unmarshaller;

    @Mock
    private Marshaller marshaller;

    @Test
    public void getDistribusjonsmeldinger_OK() throws Exception {

        TpsPersonData tpsPersonData = buildPersondata();
        when(messageQueueServiceFactory.createMessageQueueConsumer(ENV, REQUEST_QUEUE_SERVICE_RUTINE_ALIAS, false)).thenReturn(messageQueueConsumer);
        when(messageQueueConsumer.sendMessage(anyString(), anyLong())).thenReturn("<xml />");
        when(tpsDataS302Context.createUnmarshaller()).thenReturn(unmarshaller);
        when(tpsDataS302Context.createMarshaller()).thenReturn(marshaller);

        tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(tpsPersonData, ENV, 1L);

        verify(tpsDataS302Context).createMarshaller();
        verify(marshaller).marshal(eq(tpsPersonData), any(StringWriter.class));
        verify(tpsDataS302Context).createUnmarshaller();
        verify(unmarshaller).unmarshal(any(StringReader.class));
    }

    @Test
    public void sendDetailedMessageToTps_sendOk() throws Exception {

        when(messageQueueServiceFactory.createMessageQueueConsumer(ENV, QUEUE_NAME, true)).thenReturn(messageQueueConsumer);

        tpsDistribusjonsmeldingService.sendDetailedMessageToTps(AJOURHOLDSMELDING, MESSAGE, ENV, QUEUE_NAME, false);

        verify(messageQueueServiceFactory).createMessageQueueConsumer(ENV, QUEUE_NAME, true);
    }

    @Test
    public void sendDetailedMessageToTps_sendError() throws Exception {

        when(messageQueueServiceFactory.createMessageQueueConsumer(ENV, QUEUE_NAME, true)).thenThrow(JMSException.class);
        when(messageProvider.get(TPS_SEND_ERROR, null)).thenReturn(SEND_ERROR);

        String result = tpsDistribusjonsmeldingService.sendDetailedMessageToTps(DISTRIBUSJONSMELDING, MESSAGE, ENV, QUEUE_NAME, false);

        verify(messageQueueServiceFactory).createMessageQueueConsumer(ENV, QUEUE_NAME, true);
        verify(messageProvider).get(TPS_SEND_ERROR, null);
        assertThat(result, is(equalTo(SEND_ERROR)));
    }

    private static TpsPersonData buildPersondata() {

        TpsPersonData tpsPersonData = new TpsPersonData();
        tpsPersonData.setTpsServiceRutine(new TpsServiceRutineType());
        return tpsPersonData;
    }
}
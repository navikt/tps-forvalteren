package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;
import no.nav.tps.xjc.ctg.domain.s302.TpsServiceRutineType;

@RunWith(MockitoJUnitRunner.class)
public class TpsDistribusjonsmeldingServiceTest {

    private static final String ENV = "u5";

    @Mock
    private MessageQueueServiceFactory messageQueueServiceFactory;

    @Mock
    private Marshaller tpsDataS302Marshaller;

    @Mock
    private Unmarshaller tpsDataS302Unmarshaller;

    @InjectMocks
    private TpsDistribusjonsmeldingService tpsDistribusjonsmeldingService;

    @Mock
    private MessageQueueConsumer messageQueueConsumer;

    @Test
    public void getDistribusjonsmeldinger_OK() throws Exception {

        TpsPersonData tpsPersonData = buildPersondata();
        when(messageQueueServiceFactory.createMessageQueueConsumer(ENV, REQUEST_QUEUE_SERVICE_RUTINE_ALIAS)).thenReturn(messageQueueConsumer);
        when(messageQueueConsumer.sendMessage(anyString(), anyLong())).thenReturn("<xml />");

        tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(tpsPersonData, ENV);

        verify(tpsDataS302Marshaller).marshal(eq(tpsPersonData), any(StringWriter.class));
        verify(tpsDataS302Unmarshaller).unmarshal(any(StringReader.class));
    }

    private static TpsPersonData buildPersondata() {

        TpsPersonData tpsPersonData = new TpsPersonData();
        tpsPersonData.setTpsServiceRutine(new TpsServiceRutineType());
        return tpsPersonData;
    }
}
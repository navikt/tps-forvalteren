package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.ServiceRutineResponse;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories.DefaultServiceRutineMessageFactoryStrategy;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories.ServiceRutineMessageFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultTpsServiceRutineServiceTest {

    private static final String SERVICE_RUTINE_NAME = "TpsServiceRutineMessage";
    private static final String ENVIRONMENT         = "environment";

    private static final String REQUEST_XML         = "<requests><request>request</request></requests>";
    private static final String RESPONSE_XML        = "<responses><response>response</response></responses>";


    @Mock
    private MessageQueueServiceFactory messageQueueServiceFactoryMock;

    @Mock
    private XmlMapper xmlMapperMock;

    @Mock
    private MessageQueueConsumer messageQueueConsumerMock;

    @Mock
    private ServiceRutineMessageFactory messageFactoryMock;

    @Mock
    private Object responseObjectMock;

    @InjectMocks
    private DefaultTpsServiceRutineService defaultGetTpsServiceRutineService;


    @Before
    public void setUp() throws Exception {
        when( messageFactoryMock.createMessage(any()) ).thenReturn(REQUEST_XML);

        when( messageQueueServiceFactoryMock.createMessageQueueService(eq(ENVIRONMENT)) ).thenReturn(messageQueueConsumerMock);
        when( messageQueueConsumerMock.sendMessage(anyString()) ).thenReturn(RESPONSE_XML);

        when( xmlMapperMock.readValue(anyString(), any(Class.class)) ).thenReturn(responseObjectMock);
    }


    @Test
    public void createsARequestMessageUsingTheRequestMessageFactory() throws Exception {
        defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, new HashMap<>(), ENVIRONMENT);

        verify(messageFactoryMock).createMessage( any(DefaultServiceRutineMessageFactoryStrategy.class) );
    }

    @Test
    public void createsMessageQueueServiceUsingTheCorrectEnvironment() throws Exception {
        defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, new HashMap<>(), ENVIRONMENT);

        verify(messageQueueServiceFactoryMock).createMessageQueueService(eq(ENVIRONMENT));
    }

    @Test
    public void theGeneratedMessageIsSentToTps() throws Exception {
        defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, new HashMap<>(), ENVIRONMENT);

        verify(messageQueueConsumerMock).sendMessage(eq(REQUEST_XML));
    }

    @Test
    public void responseXmlIsConvertedToAnObject() throws Exception {
        defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, parameters(), ENVIRONMENT);

        verify(xmlMapperMock).readValue(eq(RESPONSE_XML), any(Class.class));
    }

    @Test
    public void responseXmlIsProvided() throws Exception {
        ServiceRutineResponse result = defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, parameters(), ENVIRONMENT);

        assertThat(result.getXml(), is(RESPONSE_XML));
    }

    @Test
    public void responseObjectIsProvided() throws Exception {
        ServiceRutineResponse result = defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, parameters(), ENVIRONMENT);

        assertThat(result.getData(), is(responseObjectMock));
    }

    @Test(expected = JMSException.class)
    public void exceptionDuringMessageQueueServiceCreationFailsGracefully() throws Exception {
        when(messageQueueServiceFactoryMock.createMessageQueueService(anyString())).thenThrow(JMSException.class);

        defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, parameters(), ENVIRONMENT);
    }

    @Test(expected = JMSException.class)
    public void exceptionDuringMessageSendingAreRethrown() throws Exception {
        when(messageQueueConsumerMock.sendMessage(anyString())).thenThrow(JMSException.class);

        defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, parameters(), ENVIRONMENT);
    }

    @Test(expected = IOException.class)
    public void exceptionDuringResponseParsingFailsGracefully() throws Exception {
        when(xmlMapperMock.readValue(anyString(), any(Class.class))).thenThrow(IOException.class);

        defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, parameters(), ENVIRONMENT);
    }

    private Map<String, Object> parameters() {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("serviceRutinenavn", "FS03-FDNUMMER-PERSDATA-O");
        parameters.put("aksjonsDato", "2001-11-11");
        parameters.put("aksjonsKode", "A");
        parameters.put("aksjonsKode2", "0");

        return parameters;
    }

}

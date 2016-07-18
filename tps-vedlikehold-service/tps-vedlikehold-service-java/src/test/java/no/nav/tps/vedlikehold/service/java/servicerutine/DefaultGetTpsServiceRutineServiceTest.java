package no.nav.tps.vedlikehold.service.java.servicerutine;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.consumer.mq.services.MessageQueueService;
import no.nav.tps.vedlikehold.domain.service.ServiceRutineResponse;
import no.nav.tps.vedlikehold.service.command.servicerutiner.DefaultGetTpsServiceRutineService;
import no.nav.tps.vedlikehold.service.command.servicerutiner.factories.DefaultServiceRutineMessageFactoryStrategy;
import no.nav.tps.vedlikehold.service.command.servicerutiner.factories.ServiceRutineMessageFactory;
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
import static org.mockito.Mockito.*;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultGetTpsServiceRutineServiceTest {

    private static final String SERVICE_RUTINE_NAME = "TpsServiceRutineMessage";
    private static final String ENVIRONMENT         = "environment";

    private static final String REQUEST_XML         = "<requests><request>request</request></requests>";
    private static final String RESPONSE_XML        = "<responses><response>response</response></responses>";


    @Mock
    private MessageQueueServiceFactory messageQueueServiceFactoryMock;

    @Mock
    private XmlMapper xmlMapperMock;

    @Mock
    private MessageQueueService messageQueueServiceMock;

    @Mock
    private ServiceRutineMessageFactory messageFactoryMock;

    @Mock
    private Object responseObjectMock;

    @InjectMocks
    private DefaultGetTpsServiceRutineService defaultGetTpsServiceRutineService;


    @Before
    public void setUp() throws Exception {
        when( messageFactoryMock.createMessage(any()) ).thenReturn(REQUEST_XML);

        when( messageQueueServiceFactoryMock.createMessageQueueService(eq(ENVIRONMENT)) ).thenReturn(messageQueueServiceMock);
        when( messageQueueServiceMock.sendMessage(anyString()) ).thenReturn(RESPONSE_XML);

        when( xmlMapperMock.readValue(anyString(), any(Class.class)) ).thenReturn(responseObjectMock);
    }


    @Test
    public void createsARequestMessageUsingTheRequestMessageFactory() {
        defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, new HashMap<>(), ENVIRONMENT);

        verify(messageFactoryMock).createMessage( any(DefaultServiceRutineMessageFactoryStrategy.class) );
    }

    @Test
    public void createsMessageQueueServiceUsingTheCorrectEnvironment() throws JMSException {
        defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, new HashMap<>(), ENVIRONMENT);

        verify(messageQueueServiceFactoryMock).createMessageQueueService(eq(ENVIRONMENT));
    }

    @Test
    public void theGeneratedMessageIsSentToTps() throws JMSException {
        defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, new HashMap<>(), ENVIRONMENT);

        verify(messageQueueServiceMock).sendMessage(eq(REQUEST_XML));
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

    @Test
    public void exceptionDuringMessageQueueServiceCreationFailsGracefully() throws JMSException {
        when(messageQueueServiceFactoryMock.createMessageQueueService(anyString())).thenThrow(JMSException.class);

        defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, parameters(), ENVIRONMENT);
    }

    @Test
    public void exceptionDuringMessageSendingFailsGracefully() throws JMSException {
        when(messageQueueServiceMock.sendMessage(anyString())).thenThrow(JMSException.class);

        defaultGetTpsServiceRutineService.execute(SERVICE_RUTINE_NAME, parameters(), ENVIRONMENT);
    }

    @Test
    public void exceptionDuringResponseParsingFailsGracefully() throws IOException {
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

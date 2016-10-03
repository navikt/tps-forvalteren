package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.jms.JMSException;

import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.xml.XmlMapper;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultTpsServiceRutineServiceTest {

    private static final String ENVIRONMENT         = "environment";
    private static final String RESPONSE_XML        = "<responses><response>response</response></responses>";

    @Mock
    private MessageQueueServiceFactory messageQueueServiceFactoryMock;

    @Mock
    private XmlMapper xmlMapperMock;

    @Mock
    private TpsRequestServiceRoutine tpsRequestServiceRoutineMock;

    @Mock
    private MessageQueueConsumer messageQueueConsumerMock;

    @Mock
    private Object responseObjectMock;

    @InjectMocks
    private DefaultTpsServiceRutineService defaultGetTpsServiceRutineService;

    @Before
    public void setUp() throws Exception {
        when( messageQueueServiceFactoryMock.createMessageQueueService(eq(ENVIRONMENT)) ).thenReturn(messageQueueConsumerMock);
        when( messageQueueConsumerMock.sendMessage(anyString()) ).thenReturn(RESPONSE_XML);

        when( xmlMapperMock.readValue(anyString(), any(Class.class)) ).thenReturn(responseObjectMock);

        when(tpsRequestServiceRoutineMock.getEnvironment()).thenReturn(ENVIRONMENT);
    }

    @Test
    public void createsMessageQueueServiceUsingTheCorrectEnvironment() throws Exception {
        defaultGetTpsServiceRutineService.execute(tpsRequestServiceRoutineMock);

        verify(messageQueueServiceFactoryMock).createMessageQueueService(eq(ENVIRONMENT));
    }

    @Test
    public void aMessageIsSentToTps() throws Exception {
        defaultGetTpsServiceRutineService.execute(tpsRequestServiceRoutineMock);

        verify(messageQueueConsumerMock).sendMessage(anyString());
    }

    @Test
    public void responseXmlIsConvertedToAnObject() throws Exception {
        defaultGetTpsServiceRutineService.execute(tpsRequestServiceRoutineMock);

        verify(xmlMapperMock).readValue(eq(RESPONSE_XML), any(Class.class));
    }

    @Test
    public void responseXmlIsProvided() throws Exception {
        ServiceRoutineResponse result = defaultGetTpsServiceRutineService.execute(tpsRequestServiceRoutineMock);

        assertThat(result.getXml(), is(RESPONSE_XML));
    }

    @Test
    public void responseObjectIsProvided() throws Exception {
        ServiceRoutineResponse result = defaultGetTpsServiceRutineService.execute(tpsRequestServiceRoutineMock);

        assertThat(result.getData(), is(responseObjectMock));
    }

    @Test(expected = JMSException.class)
    public void exceptionDuringMessageQueueServiceCreationFailsGracefully() throws Exception {
        when(messageQueueServiceFactoryMock.createMessageQueueService(anyString())).thenThrow(JMSException.class);

        defaultGetTpsServiceRutineService.execute(tpsRequestServiceRoutineMock);
    }

    @Test(expected = JMSException.class)
    public void exceptionDuringMessageSendingAreRethrown() throws Exception {
        when(messageQueueConsumerMock.sendMessage(anyString())).thenThrow(JMSException.class);

        defaultGetTpsServiceRutineService.execute(tpsRequestServiceRoutineMock);
    }

    @Test(expected = IOException.class)
    public void exceptionDuringResponseParsingFailsGracefully() throws Exception {
        when(xmlMapperMock.readValue(anyString(), any(Class.class))).thenThrow(IOException.class);

        defaultGetTpsServiceRutineService.execute(tpsRequestServiceRoutineMock);
    }
}
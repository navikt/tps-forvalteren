package no.nav.tps.forvalteren.service.command.tps;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.domain.service.tps.Request;
import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsRequestConfig;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.service.command.authorisation.ForbiddenCallHandlerService;
import no.nav.tps.forvalteren.service.command.tps.transformation.TransformationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.context.TestPropertySource;

@RunWith(MockitoJUnitRunner.class)
public class DefaultTpsRequestServiceTest {

    private static final String ENVIRONMENT = "environment";

    private static final String REQUEST_XML = "<request></request>";
    private static final String RESPONSE_XML = "<responses><response>response</response></responses>";


    private static final String NAME = "name";
    private static final String USERNAME = "username";

    @Mock
    private MessageQueueServiceFactory messageQueueServiceFactoryMock;

    @Mock
    private MessageQueueConsumer messageQueueConsumerMock;

    @Mock
    private XmlMapper xmlMapperMock;

    @Mock
    private ForbiddenCallHandlerService ForbiddenCallHandlerServiceMock;

    @Mock
    private TransformationService transformationService;

    @InjectMocks
    private DefaultTpsRequestService defaultGetTpsRequestService;

    @Before
    public void setUp() throws Exception {
        when(messageQueueServiceFactoryMock.createMessageQueueConsumer(eq(ENVIRONMENT), eq(REQUEST_QUEUE_SERVICE_RUTINE_ALIAS))).thenReturn(messageQueueConsumerMock);
    }


    @Test
    public void callsAuthorisationService() throws Exception {

        TpsServiceRoutineRequest tpsRequestMock = mock(TpsServiceRoutineRequest.class);
        TpsServiceRoutineDefinitionRequest serviceRoutine = createDefaultServiceRoutine();
        TpsRequestContext context = createDefaultContext();

        when(tpsRequestMock.getServiceRutinenavn()).thenReturn("servicerutine");
        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestMock, serviceRoutine, context);

        verify(ForbiddenCallHandlerServiceMock).authoriseRestCall(serviceRoutine);
    }


    @Test
    public void callsTransformeServiceWithRequestBeforeMessageIsSent() throws Exception {

        InOrder inOrder = inOrder(transformationService, messageQueueConsumerMock);

        TpsServiceRoutineRequest tpsRequestMock = mock(TpsServiceRoutineRequest.class);
        TpsServiceRoutineDefinitionRequest serviceRoutine = createDefaultServiceRoutine();
        TpsRequestContext context = createDefaultContext();

        when(tpsRequestMock.getServiceRutinenavn()).thenReturn("servicerutine");
        when(xmlMapperMock.writeValueAsString(tpsRequestMock)).thenReturn(REQUEST_XML);

        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestMock, serviceRoutine, context);


        inOrder.verify(transformationService).transform(any(Request.class), eq(serviceRoutine));
        inOrder.verify(messageQueueConsumerMock).sendMessage(REQUEST_XML);
    }

    @Test
    public void callsTransformServiceWithResponseAfterMessageIsSent() throws Exception {
        InOrder inOrder = inOrder(messageQueueConsumerMock, transformationService);

        TpsServiceRoutineRequest tpsRequestMock = mock(TpsServiceRoutineRequest.class);
        TpsServiceRoutineDefinitionRequest serviceRoutine = createDefaultServiceRoutine();
        TpsRequestContext context = createDefaultContext();

        when(tpsRequestMock.getServiceRutinenavn()).thenReturn("servicerutine");
        when(xmlMapperMock.writeValueAsString(tpsRequestMock)).thenReturn(REQUEST_XML);

        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestMock, serviceRoutine, context);

        when(messageQueueConsumerMock.sendMessage(REQUEST_XML)).thenReturn(RESPONSE_XML);

        inOrder.verify(messageQueueConsumerMock).sendMessage(REQUEST_XML);
        inOrder.verify(transformationService).transform(any(Response.class), eq(serviceRoutine));


    }

    @Test
    public void callsCorrectServicerutineWithTestdata() throws Exception {
        InOrder inOrder = inOrder(messageQueueConsumerMock, transformationService);

        TpsServiceRoutineRequest tpsRequestMock = mock(TpsServiceRoutineRequest.class);
        TpsServiceRoutineDefinitionRequest serviceRoutine = createDefaultServiceRoutine();
        TpsRequestContext context = createDefaultContext();

        when(tpsRequestMock.getServiceRutinenavn()).thenReturn("servicerutine-TESTDATA");
        when(xmlMapperMock.writeValueAsString(tpsRequestMock)).thenReturn(REQUEST_XML);

        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestMock, serviceRoutine, context);

        when(messageQueueConsumerMock.sendMessage(REQUEST_XML)).thenReturn(RESPONSE_XML);

        inOrder.verify(messageQueueConsumerMock).sendMessage(REQUEST_XML);
        inOrder.verify(transformationService).transform(any(Response.class), eq(serviceRoutine));

    }


    private TpsRequestContext createDefaultContext() {

        User user = new User(NAME, USERNAME);

        TpsRequestContext context = new TpsRequestContext();
        context.setUser(user);
        context.setEnvironment(ENVIRONMENT);
        return context;
    }

    private TpsServiceRoutineDefinitionRequest createDefaultServiceRoutine() {
        TpsRequestConfig config = new TpsRequestConfig();
        config.setRequestQueue(REQUEST_QUEUE_SERVICE_RUTINE_ALIAS);

        TpsServiceRoutineDefinitionRequest serviceRoutine = new TpsServiceRoutineDefinitionRequest();
        serviceRoutine.setConfig(config);

        return serviceRoutine;
    }

}
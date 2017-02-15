package no.nav.tps.vedlikehold.service.command.tps;

import com.fasterxml.jackson.xml.XmlMapper;
import com.google.common.collect.Sets;
import no.nav.tps.vedlikehold.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.vedlikehold.consumer.mq.factories.MessageQueueServiceFactory;
import no.nav.tps.vedlikehold.domain.service.User.User;
import no.nav.tps.vedlikehold.domain.service.tps.Request;
import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.config.TpsRequestConfig;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.vedlikehold.service.command.tps.transformation.TransformationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static no.nav.tps.vedlikehold.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultTpsRequestServiceTest {

    private static final String ENVIRONMENT         = "environment";


    private static final String REQUEST_XML = "<request></request>";
    private static final String RESPONSE_XML        = "<responses><response>response</response></responses>";


    private static final String NAME = "name";
    private static final String USERNAME = "username";

    private static final String ROLE_1 = "role1";
    private static final String ROLE_2 = "role2";



    @Mock
    private MessageQueueServiceFactory messageQueueServiceFactoryMock;

    @Mock
    private MessageQueueConsumer messageQueueConsumerMock;

    @Mock
    private XmlMapper xmlMapperMock;

    @Mock
    private TpsAuthorisationService tpsAuthorisationServiceMock;

    @Mock
    private TransformationService transformationService;

    @InjectMocks
    private DefaultTpsRequestService defaultGetTpsRequestService;

    @Before
    public void setUp() throws Exception {
        when(messageQueueServiceFactoryMock.createMessageQueueService(eq(ENVIRONMENT), eq(REQUEST_QUEUE_SERVICE_RUTINE_ALIAS))).thenReturn(messageQueueConsumerMock);
    }


    @Test
    public void callsAuthorisationService() throws Exception {

        TpsServiceRoutineRequest tpsRequestMock = mock(TpsServiceRoutineRequest.class);
        TpsServiceRoutineDefinition serviceRoutine = createDefaultServiceRoutine();
        TpsRequestContext context = createDefaultContext();

        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestMock, serviceRoutine, context);

        verify(tpsAuthorisationServiceMock).authoriseRestCall(serviceRoutine, context.getEnvironment(), context.getUser());
    }


    @Test
    public void callsTransformeServiceWithRequestBeforeMessageIsSent() throws Exception {

        InOrder inOrder = inOrder(transformationService, messageQueueConsumerMock);

        TpsServiceRoutineRequest tpsRequestMock = mock(TpsServiceRoutineRequest.class);
        TpsServiceRoutineDefinition serviceRoutine = createDefaultServiceRoutine();
        TpsRequestContext context = createDefaultContext();

        when(xmlMapperMock.writeValueAsString(tpsRequestMock)).thenReturn(REQUEST_XML);

        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestMock, serviceRoutine, context);


        inOrder.verify(transformationService).transform(any(Request.class), eq(serviceRoutine));
        inOrder.verify(messageQueueConsumerMock).sendMessage(REQUEST_XML);
    }

    @Test
    public void callsTransformServiceWithResponseAfterMessageIsSent() throws Exception {
        InOrder inOrder = inOrder(messageQueueConsumerMock, transformationService);

        TpsServiceRoutineRequest tpsRequestMock = mock(TpsServiceRoutineRequest.class);
        TpsServiceRoutineDefinition serviceRoutine = createDefaultServiceRoutine();
        TpsRequestContext context = createDefaultContext();

        when(xmlMapperMock.writeValueAsString(tpsRequestMock)).thenReturn(REQUEST_XML);

        defaultGetTpsRequestService.executeServiceRutineRequest(tpsRequestMock, serviceRoutine, context);

        when(messageQueueConsumerMock.sendMessage(REQUEST_XML)).thenReturn(RESPONSE_XML);

        inOrder.verify(messageQueueConsumerMock).sendMessage(REQUEST_XML);
        inOrder.verify(transformationService).transform(any(Response.class), eq(serviceRoutine));


    }

    private TpsRequestContext createDefaultContext() {

        User user = new User(NAME, USERNAME, Sets.newHashSet(ROLE_1, ROLE_2));

        TpsRequestContext context = new TpsRequestContext();
        context.setUser(user);
        context.setEnvironment(ENVIRONMENT);
        return context;
    }

    private TpsServiceRoutineDefinition createDefaultServiceRoutine() {
        TpsRequestConfig config = new TpsRequestConfig();
        config.setRequestQueue(REQUEST_QUEUE_SERVICE_RUTINE_ALIAS);

        TpsServiceRoutineDefinition serviceRoutine = new TpsServiceRoutineDefinition();
        serviceRoutine.setConfig(config);

        return serviceRoutine;
    }

}
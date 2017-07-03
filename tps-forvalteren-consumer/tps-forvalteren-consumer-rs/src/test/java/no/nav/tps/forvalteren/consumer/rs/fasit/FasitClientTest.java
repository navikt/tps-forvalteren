package no.nav.tps.forvalteren.consumer.rs.fasit;

import com.google.common.cache.Cache;
import no.nav.aura.envconfig.client.FasitRestClient;
import no.nav.aura.envconfig.client.ResourceTypeDO;
import no.nav.aura.envconfig.client.rest.PropertyElement;
import no.nav.aura.envconfig.client.rest.ResourceElement;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants;
import no.nav.tps.forvalteren.domain.ws.fasit.Queue;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class FasitClientTest {

    private static final String USERNAME    = "username";
    private static final String PASSWORD    = "password";
    private static final String BASE_URL    = "url";
    private static final String ENVIRONMENT_U = "U1";
    private static final String ENVIRONMENT_Q = "Q1";
    private static final String ENVIRONMENT_T = "T1";
    private static final String ENVIRONMENT_D = "D8";
    private static final String APPLICATION = "application";

    private static final String QUEUE_MANAGER_ALIAS     = "queueManagerAlias";
    private static final String QUEUE_MANAGER_NAME      = "queueManagerName";
    private static final String QUEUE_MANAGER_HOST_NAME = "queueManagerHostName";
    private static final String QUEUE_MANAGER_PORT      = "queueManagerPort";

    private static final String QUEUE_ALIAS = "queueAlias";
    private static final String QUEUE_NAME  = "queueName";

    private static final String FASIT_DOES_NOT_ANSWER_ERROR = "Fasit does not answer";
    private static final String ENVIRONMENT_PROPERTY_VALUE = "deployedEnvironment";

    private static final String HENT_PREFIX_KOE = "_411.";


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private FasitRestClient restClientMock;

    @Mock
    private Cache<String, ResourceElement> cacheMock;

    @InjectMocks
    private FasitClient fasitClient = new FasitClient(BASE_URL, USERNAME, PASSWORD);

    @Before
    public void setUp() {
        ResourceElement queueManagerResourceElement = new ResourceElement(ResourceTypeDO.QueueManager, QUEUE_MANAGER_ALIAS);

        queueManagerResourceElement.addProperty( new PropertyElement("name", QUEUE_MANAGER_NAME));
        queueManagerResourceElement.addProperty( new PropertyElement("hostname", QUEUE_MANAGER_HOST_NAME));
        queueManagerResourceElement.addProperty( new PropertyElement("port", QUEUE_MANAGER_PORT));

        when(restClientMock.getResource(anyString(), eq(QUEUE_MANAGER_ALIAS), eq(ResourceTypeDO.QueueManager), any(), anyString())).thenReturn(queueManagerResourceElement);


        ResourceElement queueResourceElement = new ResourceElement(ResourceTypeDO.Queue, QUEUE_ALIAS);

        queueResourceElement.addProperty( new PropertyElement("queueName", QUEUE_NAME));
        queueResourceElement.addProperty( new PropertyElement("queueManager", QUEUE_MANAGER_NAME));

        when(restClientMock.getResource(anyString(), eq(QUEUE_ALIAS), eq(ResourceTypeDO.Queue), any(), anyString())).thenReturn(queueResourceElement);
    }

    @Test
    public void queueManagerContainsAllDataFromTheRetrievedResource() {
        QueueManager queueManager = fasitClient.getApplication(APPLICATION, ENVIRONMENT_U)
                                               .getQueueManager(QUEUE_MANAGER_ALIAS);

        assertThat(queueManager.getName(), is(QUEUE_MANAGER_NAME));
        assertThat(queueManager.getHostname(), is(QUEUE_MANAGER_HOST_NAME));
        assertThat(queueManager.getPort(), is(QUEUE_MANAGER_PORT));
    }

    @Test
    public void hvisUerMiljoeSaaRetuneresEnKoeMotD8() {
        Queue queue = fasitClient.getApplication(APPLICATION, ENVIRONMENT_U)
                                 .getQueue(QUEUE_ALIAS);

        assertTrue(queue.getName().contains(ENVIRONMENT_D));
        assertTrue(queue.getName().equals("QA.D8_412."+QUEUE_ALIAS));
    }

    @Test
    public void hvisTerMiljoeOgManOnskerABrukeHentKoeSaaReturneresHentKoe() {
        Queue queue = fasitClient.getApplication(APPLICATION, ENVIRONMENT_T)
                .getQueue(TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS);

        assertTrue(queue.getName().contains(ENVIRONMENT_T));
        String koe = ("QA.T1"+ HENT_PREFIX_KOE + TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS);
        assertTrue(queue.getName().equals(koe));
    }

    // Endret
//    @Test
//    public void queueContainsAllDataFromTheRetrievedResource() {
//        Queue queue = fasitClient.getApplication(APPLICATION, ENVIRONMENT_U)
//                                 .getQueue(QUEUE_ALIAS);
//
//        assertThat(queue.getName(), is(QUEUE_NAME));
//        assertThat(queue.getManager(), is(QUEUE_MANAGER_NAME));
//    }
//
//    @Test
//    public void resourcesAreAddedToCache() {
//        fasitClient.getApplication(APPLICATION, ENVIRONMENT_U)
//                   .getQueue(QUEUE_ALIAS);
//
//        verify(cacheMock).put(anyString(), any());
//    }

    @Test
    public void resourcesAreRetrievedFromCache() {
        ResourceElement queueResourceElement = new ResourceElement(ResourceTypeDO.Queue, QUEUE_ALIAS);

        queueResourceElement.addProperty( new PropertyElement("queueName", QUEUE_NAME));
        queueResourceElement.addProperty( new PropertyElement("queueManager", QUEUE_MANAGER_NAME));

        when(cacheMock.getIfPresent(anyString())).thenReturn(queueResourceElement);

        fasitClient.getApplication(APPLICATION, ENVIRONMENT_U)
                   .getQueue(QUEUE_ALIAS);

        verify(cacheMock, never()).put(anyString(), any());
        verify(restClientMock, never()).getResource(anyString(), anyString(), any(), any(), anyString());
    }

    @Test
    public void pingReturnsTrueWhenFasitRespondsNormally() throws Exception {
        ReflectionTestUtils.setField(fasitClient, ENVIRONMENT_PROPERTY_VALUE, "u");

        ResourceElement queueResourceElement = new ResourceElement(ResourceTypeDO.Queue, QUEUE_ALIAS);

        when(restClientMock.getResource(anyString(), anyString(), any(), any(), anyString())).thenReturn(queueResourceElement);

        boolean result = fasitClient.ping();

        assertThat(result, is(true));
    }

    @Test
    public void pingThrowsExceptionWhenFasitThrowsException() throws Exception {
        ReflectionTestUtils.setField(fasitClient, ENVIRONMENT_PROPERTY_VALUE, "u");

        RuntimeException thrownException = new RuntimeException(FASIT_DOES_NOT_ANSWER_ERROR);

        when(restClientMock.getResource(anyString(), anyString(), any(), any(), anyString())).thenThrow(thrownException);


        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(FASIT_DOES_NOT_ANSWER_ERROR);

        boolean result = fasitClient.ping();

        assertThat(result, is(false));
    }

}

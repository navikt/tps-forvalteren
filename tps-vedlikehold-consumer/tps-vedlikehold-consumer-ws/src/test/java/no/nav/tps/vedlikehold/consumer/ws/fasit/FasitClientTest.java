package no.nav.tps.vedlikehold.consumer.ws.fasit;

import com.google.common.cache.Cache;
import no.nav.aura.envconfig.client.DomainDO;
import no.nav.aura.envconfig.client.FasitRestClient;
import no.nav.aura.envconfig.client.ResourceTypeDO;
import no.nav.aura.envconfig.client.rest.PropertyElement;
import no.nav.aura.envconfig.client.rest.ResourceElement;
import no.nav.tps.vedlikehold.domain.ws.fasit.Queue;
import no.nav.tps.vedlikehold.domain.ws.fasit.QueueManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class FasitClientTest {

    private static final String USERNAME    = "username";
    private static final String PASSWORD    = "password";
    private static final String BASE_URL    = "url";
    private static final String ENVIRONMENT = "u1";
    private static final String APPLICATION = "application";

    private static final String QUEUE_MANAGER_ALIAS     = "queueManagerAlias";
    private static final String QUEUE_MANAGER_NAME      = "queueManagerName";
    private static final String QUEUE_MANAGER_HOST_NAME = "queueManagerHostName";
    private static final String QUEUE_MANAGER_PORT      = "queueManagerPort";

    private static final String QUEUE_ALIAS = "queueAlias";
    private static final String QUEUE_NAME  = "queueName";


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

        when(restClientMock.getResource(anyString(), eq(QUEUE_MANAGER_ALIAS), eq(ResourceTypeDO.QueueManager), (DomainDO) any(), anyString())).thenReturn(queueManagerResourceElement);


        ResourceElement queueResourceElement = new ResourceElement(ResourceTypeDO.Queue, QUEUE_ALIAS);

        queueResourceElement.addProperty( new PropertyElement("queueName", QUEUE_NAME));
        queueResourceElement.addProperty( new PropertyElement("queueManager", QUEUE_MANAGER_NAME));

        when(restClientMock.getResource(anyString(), eq(QUEUE_ALIAS), eq(ResourceTypeDO.Queue), (DomainDO) any(), anyString())).thenReturn(queueResourceElement);
    }

    @Test
    public void queueManagerContainsAllDataFromTheRetrievedResource() {
        QueueManager queueManager = fasitClient.getApplication(APPLICATION, ENVIRONMENT)
                                               .getQueueManager(QUEUE_MANAGER_ALIAS);

        assertThat(queueManager.getName(), is(QUEUE_MANAGER_NAME));
        assertThat(queueManager.getHostname(), is(QUEUE_MANAGER_HOST_NAME));
        assertThat(queueManager.getPort(), is(QUEUE_MANAGER_PORT));
    }

    @Test
    public void queueContainsAllDataFromTheRetrievedResource() {
        Queue queue = fasitClient.getApplication(APPLICATION, ENVIRONMENT)
                                 .getQueue(QUEUE_ALIAS);

        assertThat(queue.getName(), is(QUEUE_NAME));
        assertThat(queue.getManager(), is(QUEUE_MANAGER_NAME));
    }

    @Test
    public void resourcesAreAddedToCache() {
        fasitClient.getApplication(APPLICATION, ENVIRONMENT)
                   .getQueue(QUEUE_ALIAS);

        verify(cacheMock).put(anyString(), (ResourceElement) any());
    }

    @Test
    public void resourcesAreRetrievedFromCache() {
        ResourceElement queueResourceElement = new ResourceElement(ResourceTypeDO.Queue, QUEUE_ALIAS);

        queueResourceElement.addProperty( new PropertyElement("queueName", QUEUE_NAME));
        queueResourceElement.addProperty( new PropertyElement("queueManager", QUEUE_MANAGER_NAME));

        when(cacheMock.getIfPresent(anyString())).thenReturn(queueResourceElement);

        fasitClient.getApplication(APPLICATION, ENVIRONMENT)
                   .getQueue(QUEUE_ALIAS);

        verify(cacheMock, never()).put(anyString(), (ResourceElement) any());
        verify(restClientMock, never()).getResource(anyString(), anyString(), (ResourceTypeDO) any(), (DomainDO) any(), anyString());
    }

}

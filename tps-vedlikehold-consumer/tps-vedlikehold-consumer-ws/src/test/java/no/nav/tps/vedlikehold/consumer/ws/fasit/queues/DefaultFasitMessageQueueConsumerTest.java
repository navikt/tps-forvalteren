package no.nav.tps.vedlikehold.consumer.ws.fasit.queues;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import no.nav.tps.vedlikehold.consumer.ws.fasit.FasitClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultFasitMessageQueueConsumerTest {

    private final String REQUEST_QUEUE_ALIAS    = "requestQueueAlias";
    private final String RESPONSE_QUEUE_ALIAS   = "responseQueueAlias";
    private final String QUEUE_MANAGER_ALIAS    = "queueManager";
    private final String ENVIRONMENT            = "environment";
    private final String APPLICATION            = "application";

    @Mock
    private FasitClient fasitClientMock;

    @Mock
    private FasitClient.Application applicationMock;

    @InjectMocks
    private DefaultFasitMessageQueueConsumer messageQueueConsumer = new DefaultFasitMessageQueueConsumer(APPLICATION, REQUEST_QUEUE_ALIAS, RESPONSE_QUEUE_ALIAS, QUEUE_MANAGER_ALIAS);

    @Before
    public void setUp() {
        when( fasitClientMock.getApplication(anyString(), anyString()) ).thenReturn(applicationMock);
    }

    @Test
    public void getRequestQueueGetsQueueUsingTheRequestQueueAlias() {
        messageQueueConsumer.getRequestQueue(ENVIRONMENT);

        verify(applicationMock).getQueue(REQUEST_QUEUE_ALIAS);
    }

    @Test
    public void getResponseQueueGetsQueueUsingTheResponseQueueAlias() {
        messageQueueConsumer.getResponseQueue(ENVIRONMENT);

        verify(applicationMock).getQueue(RESPONSE_QUEUE_ALIAS);
    }

    @Test
    public void getQueueManagerRetrievesManagerFromTheApplication() {
        messageQueueConsumer.getQueueManager(ENVIRONMENT);
        verify(applicationMock).getQueueManager(QUEUE_MANAGER_ALIAS);
    }
}

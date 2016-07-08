package no.nav.tps.vedlikehold.consumer.ws.fasit;

import com.google.common.cache.Cache;
import no.nav.tps.vedlikehold.consumer.ws.fasit.queue.DefaultFasitMessageQueueConsumer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultFasitMessageQueueConsumerTest {

    private final String REQUEST_QUEUE_ALIAS    = "requestQueueAlias";
    private final String RESPONSE_QUEUE_ALIAS   = "responseQueueAlias";
    private final String ENVIRONMENT            = "env";

    @Mock
    private FasitClient fasitClientMock;

    @Mock
    private Cache<String, Object> cacheMock;

    @Mock
    private FasitClient.Application applicationMock;

    @InjectMocks
    private DefaultFasitMessageQueueConsumer messageQueueConsumer = new DefaultFasitMessageQueueConsumer("tpsws", REQUEST_QUEUE_ALIAS, RESPONSE_QUEUE_ALIAS);

    @Before
    public void setUp() {
        when( fasitClientMock.getApplication(anyString(), anyString()) ).thenReturn(applicationMock);
    }

    /* Request queue */

    @Test
    public void getRequestQueueGetsQueueUsingTheRequestQueueAlias() {
        messageQueueConsumer.getRequestQueue(ENVIRONMENT);

        verify(applicationMock).getQueue(REQUEST_QUEUE_ALIAS);
    }

    /* Response queue */

    @Test
    public void getResponseQueueGetsQueueUsingTheResponseQueueAlias() {
        messageQueueConsumer.getResponseQueue(ENVIRONMENT);

        verify(applicationMock).getQueue(RESPONSE_QUEUE_ALIAS);
    }

}

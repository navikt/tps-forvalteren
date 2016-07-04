package no.nav.tps.vedlikehold.consumer.rs.fasit;

import com.google.common.cache.Cache;
import no.nav.brevogarkiv.common.fasit.FasitClient;
import no.nav.tps.vedlikehold.consumer.rs.fasit.queue.DefaultFasitQueueConsumer;
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
public class DefaultFasitQueueConsumerTest {


    private static final String ENVIRONMENT = "u1";
    private static final String QUEUE_ALIAS = "queueAlias";
    private static final String QUEUE       = "queue";

    @Mock
    private FasitClient fasitClientMock;

    @Mock
    private Cache<String, Object> cacheMock;

    @Mock
    private FasitClient.Application applicationMock;

    @InjectMocks
    private DefaultFasitQueueConsumer consumer = new DefaultFasitQueueConsumer("tpsws");

    @Before
    public void setUp() {
        when( fasitClientMock.getApplication(anyString(), anyString()) ).thenReturn(applicationMock);
        when( cacheMock.getIfPresent( anyString() ) ).thenReturn(null);
        when( applicationMock.getQueue( eq(QUEUE_ALIAS) ) ).thenReturn(QUEUE);
    }


    /* Get request queue */

    @Test
    public void getQueueReturnsQueueFromApplicationIfCacheIsEmpty() {
        String queue = consumer.getQueue(QUEUE_ALIAS, ENVIRONMENT);
        assertThat("the retrieved queue is the one the application provides", queue.equals(QUEUE));
        verify(applicationMock).getQueue( anyString() );
    }

    @Test
    public void getQueueUpdatesCacheIfCacheIsEmpty() {
        String queue = consumer.getQueue(QUEUE_ALIAS, ENVIRONMENT);
        verify(cacheMock).put( anyString(), eq(queue) );
    }

    @Test
    public void getQueueReturnsQueueFromCacheIfPresent() {
        when( cacheMock.getIfPresent( anyString() ) ).thenReturn(QUEUE);

        String queue = consumer.getQueue(QUEUE_ALIAS, ENVIRONMENT);
        assertThat("the retrieved queue is the one the cache provides", queue.equals(QUEUE));

        verify(cacheMock).getIfPresent( anyString() );
        verify(applicationMock, never()).getQueue( anyString() );
    }

    @Test
    public  void getQueueReturnsNullIfApplicationDoesNotExist() {
        when( fasitClientMock.getApplication(anyString(), anyString()) ).thenReturn(null);
        assertThat("queue is null for a nonexistent application", consumer.getQueue(QUEUE_ALIAS, ENVIRONMENT) == null );
    }

}

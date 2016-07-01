package no.nav.tps.vedlikehold.consumer.rs.fasit;

import com.google.common.cache.Cache;
import no.nav.brevogarkiv.common.fasit.FasitClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class FasitConsumerTest {


    private static final String ENVIRONMENT = "u1";
    private static final String REQUEST_QUEUE = "requestQueue";
    private static final String RESPONSE_QUEUE = "responseQueue";

    @Mock
    private FasitClient fasitClientMock;

    @Mock
    private Cache<String, Object> cacheMock;

    @Mock
    private FasitClient.Application applicationMock;

    @InjectMocks
    private FasitConsumer consumer;

    @Before
    public void setUp() {
        when( fasitClientMock.getApplication(anyString(), anyString()) ).thenReturn(applicationMock);
        when( cacheMock.getIfPresent( anyString() ) ).thenReturn(null);
    }


    /* Get request queue */

    @Test
    public void getRequestQueueReturnsQueueFromApplicationIfCacheIsEmpty() {
        consumer.getRequestQueue(ENVIRONMENT);
        verify(applicationMock).getQueue( anyString() );
    }

    @Test
    public void getRequestQueueUpdatesCacheIfCacheIsEmpty() {
        String queue = consumer.getRequestQueue(ENVIRONMENT);
        verify(cacheMock).put( anyString(), eq(queue) );
    }

    @Test
    public void getRequestQueueReturnsQueueFromCacheIfPresent() {
        when( cacheMock.getIfPresent( anyString() ) ).thenReturn(REQUEST_QUEUE);

        consumer.getRequestQueue(ENVIRONMENT);
        verify(cacheMock).getIfPresent( anyString() );
        verify(applicationMock, never()).getQueue( anyString() );
    }

    @Test
    public  void getRequestQueueReturnsNullIfApplicationDoesNotExist() {
        when( fasitClientMock.getApplication(anyString(), anyString()) ).thenReturn(null);
        assertThat("queue is null for a nonexistent application", consumer.getRequestQueue(ENVIRONMENT) == null );
    }

    /* Get response queue */

    @Test
    public void getResponseQueueReturnsQueueFromApplicationIfCacheIsEmpty() {
        consumer.getResponseQueue(ENVIRONMENT);
        verify(applicationMock).getQueue( anyString() );
    }

    @Test
    public void getResponseQueueUpdatesCacheIfCacheIsEmpty() {
        String queue = consumer.getResponseQueue(ENVIRONMENT);
        verify(cacheMock).put( anyString(), eq(queue) );
    }

    @Test
    public void getResponseQueueReturnsQueueFromCacheIfPresent() {
        when( cacheMock.getIfPresent( anyString() ) ).thenReturn(RESPONSE_QUEUE);

        consumer.getResponseQueue(ENVIRONMENT);
        verify(cacheMock).getIfPresent( anyString() );
        verify(applicationMock, never()).getQueue( anyString() );
    }

    @Test
    public  void getResponseQueueReturnsNullIfApplicationDoesNotExist() {
        when( fasitClientMock.getApplication(anyString(), anyString()) ).thenReturn(null);
        assertThat("queue is null for a nonexistent application", consumer.getResponseQueue(ENVIRONMENT) == null );
    }

}

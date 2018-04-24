package no.nav.tps.forvalteren.consumer.rs.fasit.queues;

import no.nav.tps.forvalteren.consumer.rs.fasit.FasitClient;
import no.nav.tps.forvalteren.domain.ws.fasit.Queue;
import no.nav.tps.forvalteren.domain.ws.fasit.QueueManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class FasitMessageQueueConsumerTest {

    private final String REQUEST_QUEUE_ALIAS = "requestQueueAlias";
    private final String QUEUE_MANAGER_ALIAS = "queueManager";
    private final String ENVIRONMENT = "environment";
    private final String APPLICATION = "application";

    @Mock
    private FasitClient fasitClientMock;

    @Mock
    private QueueManager queueManagerMock;

    @Mock
    private Queue requestQueueMock;

//    @Mock
//    private FasitClient.Application applicationMock;

//    @InjectMocks
//    private DefaultFasitMessageQueueConsumer consumer = new DefaultFasitMessageQueueConsumer(APPLICATION, REQUEST_QUEUE_ALIAS, QUEUE_MANAGER_ALIAS);

    @Before
    public void setUp() {
//        when(fasitClientMock.getApplication(anyString(), anyString())).thenReturn(applicationMock);
//        when(applicationMock.getQueueManager()).thenReturn(queueManagerMock);
//        when(applicationMock.getQueue(REQUEST_QUEUE_ALIAS)).thenReturn(requestQueueMock);
    }

    @Test
    public void getRequestQueueGetsQueueUsingTheRequestQueueAlias() {
//        Queue result = consumer.getRequestQueue(ENVIRONMENT);
//
//        assertThat(result, is(requestQueueMock));
    }
//
//    @Test
//    public void getQueueManagerRetrievesManagerFromTheApplication() {
//        QueueManager result = consumer.getQueueManager(ENVIRONMENT);
//
//        assertThat(result, is(queueManagerMock));
//    }
//
//    @Test
//    public void getQueueManagerRetrievesManagerWithAlias() {
//        QueueManager result = consumer.getQueueManager(QUEUE_MANAGER_ALIAS, ENVIRONMENT);
//
//        assertThat(result, is(queueManagerMock));
//    }

}

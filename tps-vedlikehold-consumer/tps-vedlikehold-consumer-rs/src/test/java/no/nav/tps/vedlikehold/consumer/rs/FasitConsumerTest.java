package no.nav.tps.vedlikehold.consumer.rs;

import no.nav.brevogarkiv.common.fasit.FasitClient;
import no.nav.tps.vedlikehold.consumer.rs.fasit.FasitConsumer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

/**
 * Created by Øyvind Grimnes, Visma Consulting AS on 29.06.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class FasitConsumerTest {

    private static final String ENVIRONMENT_DEVELOPMENT = "u1";

    @Mock
    private FasitClient fasitClientMock;

    @Mock
    private FasitClient.Application applicationMock;

    @InjectMocks
    private FasitConsumer consumer;

    @Before
    public void setUp() {

    }

    @Test
    public void getApplicationCallsGetApplicationOnFasitClient() {
        consumer.getApplication(ENVIRONMENT_DEVELOPMENT);
        verify(fasitClientMock).getApplication( eq(ENVIRONMENT_DEVELOPMENT), anyString() );
    }

    @Test
    public void getApplicationCallsReturnsApplicationFromFasitClient() {
        when( fasitClientMock.getApplication( anyString(), anyString()) ).thenReturn( applicationMock );
        FasitClient.Application application = consumer.getApplication(ENVIRONMENT_DEVELOPMENT);
        assertThat(application, is(applicationMock));
    }

}

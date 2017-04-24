package no.nav.tps.forvalteren.service.command.fasit;

import no.nav.tps.forvalteren.consumer.rs.fasit.FasitClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class PingFasitTest {
    @Mock
    private FasitClient consumerMock;

    @InjectMocks
    private PingFasit command;

    @Test
    public void callsPingOnConsumerWhenExecuting() throws Exception{
        command.execute();

        verify(consumerMock).ping();
    }
}

package no.nav.tps.forvalteren.service.command.vera;


import no.nav.tps.forvalteren.consumer.rs.vera.VeraConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GetEnvironmentsTest {

    private static final String APPLICATION = "tpsws";

    @Mock
    private VeraConsumer veraConsumerMock;

    @InjectMocks
    GetEnvironments getEnvironments;


    @Test
    public void callsGetEnvironmentsOnVeraConsumer() {
        getEnvironments.execute(APPLICATION);

        verify(veraConsumerMock).getEnvironments(eq(APPLICATION));
    }
}

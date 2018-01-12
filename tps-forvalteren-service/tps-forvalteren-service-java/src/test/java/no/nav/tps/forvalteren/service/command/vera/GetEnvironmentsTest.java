package no.nav.tps.forvalteren.service.command.vera;


import no.nav.tps.forvalteren.consumer.rs.environments.FetchEnvironmentsConsumer;
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
    private FetchEnvironmentsConsumer veraConsumerMock;

    @InjectMocks
    GetEnvironments getEnvironments;


    @Test
    public void callsGetEnvironmentsOnVeraConsumer() {
        getEnvironments.getEnvironmentsFromFasit(APPLICATION);

        verify(veraConsumerMock).getEnvironments(eq(APPLICATION));
    }
}

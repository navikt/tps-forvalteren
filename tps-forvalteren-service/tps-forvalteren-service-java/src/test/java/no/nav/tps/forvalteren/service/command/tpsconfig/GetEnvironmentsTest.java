package no.nav.tps.forvalteren.service.command.tpsconfig;


import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;

@RunWith(MockitoJUnitRunner.class)
public class GetEnvironmentsTest {

    private static final String APPLICATION = "tpsws";

    @Mock
    private FasitApiConsumer veraConsumerMock;

    @InjectMocks
    GetEnvironments getEnvironments;


    @Test
    public void callsGetEnvironmentsOnVeraConsumer() {
        getEnvironments.getEnvironmentsFromFasit(APPLICATION);

        verify(veraConsumerMock).getEnvironments(eq(APPLICATION));
    }
}

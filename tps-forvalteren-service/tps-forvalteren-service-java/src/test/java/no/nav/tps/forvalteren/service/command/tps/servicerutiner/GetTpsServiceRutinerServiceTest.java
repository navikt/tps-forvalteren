package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.ServiceRoutineResolver;

@RunWith(MockitoJUnitRunner.class)
public class GetTpsServiceRutinerServiceTest {

    @Mock
    private TpsServiceRoutineDefinitionRequest routineMock;

    @Mock
    private ServiceRoutineResolver resolverMock;

    @Spy
    private List<ServiceRoutineResolver> resolvers = new ArrayList<>();

    @InjectMocks
    private GetTpsServiceRutinerService command;

    @Before
    public void before() {
        when(resolverMock.resolve()).thenReturn(routineMock);
        resolvers.clear();
        resolvers.add(resolverMock);
    }

    @Test
    public void returnsRoutines() {
        List<TpsServiceRoutineDefinitionRequest> routines = command.execute();

        assertThat(routines, contains(routineMock));
    }
}
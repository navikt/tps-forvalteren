package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers.ServiceRoutineResolver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultGetTpsServiceRutinerServiceTest {

    @Mock
    private TpsServiceRoutine routineMock;

    @Mock
    private ServiceRoutineResolver resolverMock;

    @Spy
    private List<ServiceRoutineResolver> resolvers = new ArrayList<>();

    @InjectMocks
    private DefaultGetTpsServiceRutinerService command;

    @Before
    public void before() {
        when(resolverMock.resolve()).thenReturn(routineMock);
        resolvers.clear();
        resolvers.add(resolverMock);
    }

    @Test
    public void returnsRoutines() {
        Collection<TpsServiceRoutine> routines = command.exectue();

        assertThat(routines, contains(routineMock));
    }
}
package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceRoutineControllerTest {

    @Mock
    private GetTpsServiceRutinerService serviceMock;

    @InjectMocks
    private ServiceRoutineController controller;

    @Test
    public void returnsResultFromService() {
        List<TpsServiceRoutine> routines = Collections.emptyList();
        when(serviceMock.exectue()).thenReturn(routines);

        List<TpsServiceRoutine> result = controller.getTpsServiceRutiner();

        assertThat(result, is(sameInstance(routines)));
    }
}
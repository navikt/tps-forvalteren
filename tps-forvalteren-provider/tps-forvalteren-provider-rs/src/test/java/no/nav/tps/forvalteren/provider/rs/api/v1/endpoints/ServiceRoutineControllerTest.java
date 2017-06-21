package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.forvalteren.service.command.authorisation.TpsAuthorisationService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.GetTpsServiceRutinerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceRoutineControllerTest {

    @Mock
    private TpsAuthorisationService tpsAuthorisationServiceMock;

    @Mock
    private GetTpsServiceRutinerService getTpsServiceRutinerServiceMock;

    @InjectMocks
    private ServiceRoutineController serviceRoutineController;

    @Test
    public void returnsResultFromService() {
        TpsServiceRoutineDefinition serviceRoutine = mock(TpsServiceRoutineDefinition.class);

        List<TpsServiceRoutineDefinition> serviceRoutines = Collections.singletonList(serviceRoutine);
        when(getTpsServiceRutinerServiceMock.execute()).thenReturn(serviceRoutines);

        when(tpsAuthorisationServiceMock.isAuthorisedToUseServiceRutine(serviceRoutine)).thenReturn(true);

        List<TpsServiceRoutineDefinition> result = serviceRoutineController.getTpsServiceRutiner();

        assertThat(result, hasItem(serviceRoutine));
    }


    @Test
    public void getTpsServiceRutinerReturnererBareLovligeServiceRutiner() throws Exception {
        TpsServiceRoutineDefinition serviceRoutineMock1 = mock(TpsServiceRoutineDefinition.class);
        TpsServiceRoutineDefinition serviceRoutineMock2 = mock(TpsServiceRoutineDefinition.class);
        TpsServiceRoutineDefinition serviceRoutineMock3 = mock(TpsServiceRoutineDefinition.class);
        when(getTpsServiceRutinerServiceMock.execute()).thenReturn(Arrays.asList(serviceRoutineMock1, serviceRoutineMock2, serviceRoutineMock3));

        when(tpsAuthorisationServiceMock.isAuthorisedToUseServiceRutine(serviceRoutineMock1)).thenReturn(false);
        when(tpsAuthorisationServiceMock.isAuthorisedToUseServiceRutine(serviceRoutineMock2)).thenReturn(true);
        when(tpsAuthorisationServiceMock.isAuthorisedToUseServiceRutine(serviceRoutineMock3)).thenReturn(true);

        List<TpsServiceRoutineDefinition> rutiner = serviceRoutineController.getTpsServiceRutiner();

        assertSame(rutiner.size() , 2);
        assertThat(rutiner, containsInAnyOrder(serviceRoutineMock2, serviceRoutineMock3));
    }
}
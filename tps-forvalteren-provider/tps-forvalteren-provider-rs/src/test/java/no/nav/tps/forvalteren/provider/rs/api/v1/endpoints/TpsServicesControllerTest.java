package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.service.command.authorisation.ForbiddenCallHandlerService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.GetTpsServiceRutinerService;

@RunWith(MockitoJUnitRunner.class)
public class TpsServicesControllerTest {

    @Mock
    private ForbiddenCallHandlerService ForbiddenCallHandlerServiceMock;

    @Mock
    private GetTpsServiceRutinerService getTpsServiceRutinerServiceMock;

    @InjectMocks
    private TpsServicesController tpsServicesController;

    @Test
    public void returnsResultFromService() {
        TpsServiceRoutineDefinitionRequest serviceRoutine = mock(TpsServiceRoutineDefinitionRequest.class);

        List<TpsServiceRoutineDefinitionRequest> serviceRoutines = Collections.singletonList(serviceRoutine);
        when(getTpsServiceRutinerServiceMock.execute()).thenReturn(serviceRoutines);

        when(ForbiddenCallHandlerServiceMock.isAuthorisedToUseServiceRutine(serviceRoutine)).thenReturn(true);

        List<TpsServiceRoutineDefinitionRequest> result = tpsServicesController.getTpsServicesMenu();

        assertThat(result, hasItem(serviceRoutine));
    }


    @Test
    public void getTpsServiceRutinerReturnererBareLovligeServiceRutiner() throws Exception {
        TpsServiceRoutineDefinitionRequest serviceRoutineMock1 = mock(TpsServiceRoutineDefinitionRequest.class);
        TpsServiceRoutineDefinitionRequest serviceRoutineMock2 = mock(TpsServiceRoutineDefinitionRequest.class);
        TpsServiceRoutineDefinitionRequest serviceRoutineMock3 = mock(TpsServiceRoutineDefinitionRequest.class);
        when(getTpsServiceRutinerServiceMock.execute()).thenReturn(Arrays.asList(serviceRoutineMock1, serviceRoutineMock2, serviceRoutineMock3));

        when(ForbiddenCallHandlerServiceMock.isAuthorisedToUseServiceRutine(serviceRoutineMock1)).thenReturn(false);
        when(ForbiddenCallHandlerServiceMock.isAuthorisedToUseServiceRutine(serviceRoutineMock2)).thenReturn(true);
        when(ForbiddenCallHandlerServiceMock.isAuthorisedToUseServiceRutine(serviceRoutineMock3)).thenReturn(true);

        List<TpsServiceRoutineDefinitionRequest> rutiner = tpsServicesController.getTpsServicesMenu();

        assertSame(rutiner.size() , 2);
        assertThat(rutiner, containsInAnyOrder(serviceRoutineMock2, serviceRoutineMock3));
    }
}
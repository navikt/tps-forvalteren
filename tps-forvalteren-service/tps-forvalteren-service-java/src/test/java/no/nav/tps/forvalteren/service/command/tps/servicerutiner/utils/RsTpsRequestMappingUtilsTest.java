package no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsHentPersonServiceRoutineRequest;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.GetTpsServiceRutinerService;

@RunWith(MockitoJUnitRunner.class)
public class RsTpsRequestMappingUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    @InjectMocks
    private RsTpsRequestMappingUtils rsTpsRequestMappingUtils;

    @Test
    public void convertConvertsToTypeAndReturnsResult() {
        Object val = new Object();
        Map<String, Object> params = new HashMap<>();
        doReturn(val).when(objectMapper).convertValue(params, Object.class);

        Object result = rsTpsRequestMappingUtils.convert(params, Object.class);

        assertThat(result, is(sameInstance(val)));
    }

    @Test
    public void convertToTpsRequestMapsServiceRoutineAndReturnsMappedResult() {
        TpsServiceRoutineDefinitionRequest routine = mock(TpsServiceRoutineDefinitionRequest.class);
        doReturn("name").when(routine).getName();
        doReturn(TpsHentPersonServiceRoutineRequest.class).when(routine).getJavaClass();
        List<TpsServiceRoutineDefinitionRequest> routines = Collections.singletonList(routine);
        when(getTpsServiceRutinerService.execute()).thenReturn(routines);

        Map parametersMap = mock(Map.class);

        TpsHentPersonServiceRoutineRequest respMock = mock(TpsHentPersonServiceRoutineRequest.class);
        when(objectMapper.convertValue(parametersMap, TpsHentPersonServiceRoutineRequest.class)).thenReturn(respMock);

        TpsServiceRoutineRequest result = rsTpsRequestMappingUtils.convertToTpsServiceRoutineRequest("name", parametersMap);

        assertThat(result, is(sameInstance(respMock)));
    }
}
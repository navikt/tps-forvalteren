package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils;

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

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.hent.TpsHentPersonServiceRoutineRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class RsRequestMappingUtilsTest {

    @Mock
    private ObjectMapper objectMapperMock;

    @Mock
    private GetTpsServiceRutinerService serviceMock;

    @InjectMocks
    private RsTpsRequestMappingUtils utils;

    @Test
    public void convertConvertsToTypeAndReturnsResult() {
        Object val = new Object();
        Map<String, Object> params = new HashMap<>();
        doReturn(val).when(objectMapperMock).convertValue(params, Object.class);

        Object result = utils.convert(params, Object.class);

        assertThat(result, is(sameInstance(val)));
    }

    @Test
    public void convertToTpsRequestMapsServiceRoutineAndReturnsMappedResult() {
        TpsServiceRoutineDefinition routine = mock(TpsServiceRoutineDefinition.class);
        doReturn("name").when(routine).getName();
        doReturn(TpsHentPersonServiceRoutineRequest.class).when(routine).getJavaClass();
        List<TpsServiceRoutineDefinition> routines = Collections.singletonList(routine);
        when(serviceMock.execute()).thenReturn(routines);

        JsonNode nodeMock = mock(JsonNode.class);

        TpsHentPersonServiceRoutineRequest respMock = mock(TpsHentPersonServiceRoutineRequest.class);
        when(objectMapperMock.convertValue(nodeMock, TpsHentPersonServiceRoutineRequest.class)).thenReturn(respMock);

        TpsServiceRoutineRequest result = utils.convertToTpsServiceRoutineRequest("name", nodeMock);

        assertThat(result, is(sameInstance(respMock)));
    }
}
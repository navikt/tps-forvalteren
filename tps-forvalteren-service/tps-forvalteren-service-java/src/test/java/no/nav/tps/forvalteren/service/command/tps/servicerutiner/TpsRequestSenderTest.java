package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import no.nav.tps.forvalteren.service.command.tps.TpsRequestService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils;
import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TpsRequestSenderTest {

    @Mock
    private FindServiceRoutineByName findServiceRoutineByNameMock;

    @Mock
    private TpsRequestService tpsRequestServiceMock;

    @Mock
    private TpsServiceRoutineDefinition serviceRoutineDefinitionMock;

    @Mock
    private TpsServiceRoutineRequest tpsServiceRoutineRequestMock;

    @Mock
    private TpsRequestContext contextMock;

    @Mock
    private RsTpsResponseMappingUtils utilsMock;

    @InjectMocks
    private TpsRequestSender tpsRequestSender;

    @Test
    public void sendTpsRequest() throws Exception {
        Response response = mock(Response.class);
        Optional<TpsServiceRoutineDefinition> tpsServiceRoutineDefinition = Optional.of(serviceRoutineDefinitionMock);

        when(tpsServiceRoutineRequestMock.getServiceRutinenavn()).thenReturn("navn");
        when(findServiceRoutineByNameMock.execute(anyString())).thenReturn(tpsServiceRoutineDefinition);
        when(tpsRequestServiceMock.executeServiceRutineRequest(tpsServiceRoutineRequestMock, serviceRoutineDefinitionMock, contextMock)).thenReturn(response);

        tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequestMock,contextMock);

        verify(tpsRequestServiceMock).executeServiceRutineRequest(tpsServiceRoutineRequestMock, serviceRoutineDefinitionMock, contextMock);
        verify(utilsMock).convertToTpsServiceRutineResponse(response);
    }

}